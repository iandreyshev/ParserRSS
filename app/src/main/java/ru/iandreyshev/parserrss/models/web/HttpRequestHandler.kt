package ru.iandreyshev.parserrss.models.web

import okhttp3.HttpUrl
import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import okhttp3.Request

class HttpRequestHandler(override val urlString: String) : IHttpRequestResult {
    companion object {
        private const val MAX_CONTENT_BYTES = 5242880L // 5MB

        private const val GOOD_RESPONSE_CODE = 200
        private const val DEFAULT_PROTOCOL = "http://"

        private const val READ_TIMEOUT_SEC = 3L
        private const val CONNECTION_TIMEOUT_SEC = 3L
        private const val WRITE_TIMEOUT_SEC = 3L
    }

    enum class State {
        NotSend,
        Success,
        BadUrl,
        BadConnection,
        PermissionDenied
    }

    var maxContentBytes: Long = MAX_CONTENT_BYTES
    var readTimeoutSec: Long = READ_TIMEOUT_SEC
    var connectionTimeoutSec: Long = CONNECTION_TIMEOUT_SEC
    var writeTimeoutSec: Long = WRITE_TIMEOUT_SEC
    override var state: State = State.NotSend
        private set
    override var body: ByteArray? = null
        private set
    override val bodyAsString: String?
        get() {
            return String(body ?: return null)
        }

    private val _url: HttpUrl? = HttpUrl.parse(urlString)
            ?: HttpUrl.parse(DEFAULT_PROTOCOL + urlString)

    init {
        state = when (_url) {
            null -> State.BadUrl
            else -> State.NotSend
        }
    }

    fun sendGet(): State {
        _url ?: return State.BadUrl

        send(Request.Builder()
                .url(_url)
                .build())

        return state
    }

    private fun send(request: Request) {
        try {
            val client = OkHttpClient.Builder()
                    .readTimeout(readTimeoutSec, TimeUnit.SECONDS)
                    .connectTimeout(connectionTimeoutSec, TimeUnit.SECONDS)
                    .writeTimeout(writeTimeoutSec, TimeUnit.SECONDS)
                    .build()

            client.newCall(request).execute().use { response ->
                response.body().use { body ->
                    state = when {
                        (response.code() != GOOD_RESPONSE_CODE || body == null || body.contentLength() > maxContentBytes) ->
                            State.BadConnection
                        else -> {
                            val source = body.source()
                            source.request(maxContentBytes)
                            this.body = source.buffer().snapshot().toByteArray()
                            State.Success
                        }
                    }
                }
            }
        } catch (ex: SecurityException) {
            state = State.PermissionDenied
        } catch (ex: Exception) {
            state = State.BadConnection
        }
    }
}
