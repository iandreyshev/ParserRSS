package ru.iandreyshev.parserrss.models.web

import okhttp3.HttpUrl
import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import okhttp3.Request

class HttpRequestHandler(override val urlString: String) : IHttpRequestResult {
    companion object {
        private const val MAX_CONTENT_BYTES = 5242880 // 5MB

        private const val GOOD_RESPONSE_CODE = 200
        private const val DEFAULT_PROTOCOL = "http://"

        private const val READ_TIMEOUT_SEC = 2
        private const val CONNECTION_TIMEOUT_SEC = 2
        private const val WRITE_TIMEOUT_SEC = 2
    }

    enum class State {
        NotSend,
        Success,
        BadUrl,
        BadConnection,
        PermissionDenied
    }

    override var state: State = State.NotSend
        private set
    override var body: ByteArray? = null
        private set
    override val bodyAsString: String?
        get() = body?.toString()
    var maxContentBytes = MAX_CONTENT_BYTES

    private val client = OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT_SEC.toLong(), TimeUnit.SECONDS)
            .connectTimeout(CONNECTION_TIMEOUT_SEC.toLong(), TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SEC.toLong(), TimeUnit.SECONDS)
            .build()
    private val url: HttpUrl? = HttpUrl.parse(urlString) ?: HttpUrl.parse(DEFAULT_PROTOCOL + urlString)

    init {
        state = when (url) {
            null -> State.BadUrl
            else -> State.NotSend
        }
    }

    fun sendGet(): State {
        url ?: return State.BadUrl

        send(Request.Builder()
                .url(url)
                .build())

        return state
    }

    private fun send(request: Request) {
        try {
            client.newCall(request).execute().use { response ->
                response.body().use { body ->
                    state = when {
                        (response.code() != GOOD_RESPONSE_CODE || body == null || body.contentLength() > maxContentBytes) ->
                            State.BadConnection
                        else -> State.Success
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
