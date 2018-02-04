package ru.iandreyshev.parserrss.models.web

import okhttp3.HttpUrl
import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import okhttp3.Request

class HttpRequestHandler(urlString: String) : IHttpRequestHandler {

    companion object {
        private const val MAX_CONTENT_BYTES = 5242880L // 5MB

        private const val GOOD_RESPONSE_CODE = 200
        private const val DEFAULT_PROTOCOL = "http://"
        private const val DEFAULT_URL = ""

        private const val READ_TIMEOUT_SEC = 3L
        private const val CONNECTION_TIMEOUT_SEC = 3L
        private const val WRITE_TIMEOUT_SEC = 3L
    }

    constructor() : this(DEFAULT_URL)

    enum class State {
        NOT_SEND,
        SUCCESS,
        BAD_URL,
        BAD_CONNECTION,
        PERMISSION_DENIED
    }

    var maxContentBytes: Long = MAX_CONTENT_BYTES
    var readTimeoutSec: Long = READ_TIMEOUT_SEC
    var connectionTimeoutSec: Long = CONNECTION_TIMEOUT_SEC
    var writeTimeoutSec: Long = WRITE_TIMEOUT_SEC

    override var urlString: String = urlString
        private set
    override var state: State = State.NOT_SEND
        private set
    override var body: ByteArray? = null
        private set
    override val bodyAsString: String?
        get() {
            return String(body ?: return null)
        }

    private var mUrl: HttpUrl? = null
        set(value) {
            state = if (value == null) State.BAD_URL else State.NOT_SEND
            field = value
        }

    override fun sendGet(): State {
        mUrl = parseUrl(urlString)
        mUrl?.let {
            send(Request.Builder()
                    .url(it)
                    .build())
            return state
        }

        return State.BAD_URL
    }

    override fun sendGet(url: String): State {
        urlString = url

        return sendGet()
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
                            State.BAD_CONNECTION
                        else -> {
                            val source = body.source()
                            source.request(maxContentBytes)
                            this.body = source.buffer().snapshot().toByteArray()
                            State.SUCCESS
                        }
                    }
                }
            }
        } catch (ex: SecurityException) {
            state = State.PERMISSION_DENIED
        } catch (ex: Exception) {
            state = State.BAD_CONNECTION
        }
    }

    private fun parseUrl(url: String): HttpUrl? {
        return HttpUrl.parse(url) ?: HttpUrl.parse(DEFAULT_PROTOCOL + url)
    }
}
