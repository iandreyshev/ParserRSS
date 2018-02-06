package ru.iandreyshev.parserrss.models.web

import okhttp3.HttpUrl
import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import okhttp3.Request

class HttpRequestHandler(urlString: String) : IHttpRequestHandler {

    companion object {
        private const val DEFAULT_CONTENT_BYTES = 5242880L // 5MB

        private const val GOOD_RESPONSE_CODE = 200
        private const val DEFAULT_PROTOCOL = "http://"

        private const val DEFAULT_READ_TIMEOUT_MS = 3000L
        private const val DEFAULT_CONNECTION_TIMEOUT_MS = 3000L
        private const val DEFAULT_WRITE_TIMEOUT_MS = 3000L
    }

    enum class State {
        NOT_SEND,
        SUCCESS,
        BAD_URL,
        BAD_CONNECTION,
        PERMISSION_DENIED
    }

    override var maxContentBytes: Long = DEFAULT_CONTENT_BYTES
    override var readTimeoutMs: Long = DEFAULT_READ_TIMEOUT_MS
    override var connectionTimeoutMs: Long = DEFAULT_CONNECTION_TIMEOUT_MS
    override var writeTimeoutMs: Long = DEFAULT_WRITE_TIMEOUT_MS

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
        parseUrl(urlString)

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
                    .readTimeout(readTimeoutMs, TimeUnit.MILLISECONDS)
                    .connectTimeout(connectionTimeoutMs, TimeUnit.MILLISECONDS)
                    .writeTimeout(writeTimeoutMs, TimeUnit.MILLISECONDS)
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

    private fun parseUrl(url: String) {
        mUrl = HttpUrl.parse(url) ?: HttpUrl.parse(DEFAULT_PROTOCOL + url)
    }
}
