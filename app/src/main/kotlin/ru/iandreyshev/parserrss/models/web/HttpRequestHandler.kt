package ru.iandreyshev.parserrss.models.web

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

abstract class HttpRequestHandler : IHttpRequestResult {

    companion object {
        internal const val DEFAULT_MAX_CONTENT_BYTES = 5242880L // 5MB

        private const val OK_RESPONSE_CODE = 200
        private const val EMPTY_URL = ""
        private const val DEFAULT_PROTOCOL = "http://"

        private const val DEFAULT_MAX_READ_TIMEOUT_MS = 3000L
        private const val DEFAULT_MAX_CONNECTION_TIMEOUT_MS = 3000L
        private const val DEFAULT_MAX_WRITE_TIMEOUT_MS = 3000L
    }

    enum class State {
        NOT_SEND,
        SUCCESS,
        BAD_URL,
        BAD_CONNECTION,
        PERMISSION_DENIED
    }

    var maxContentBytes: Long = DEFAULT_MAX_CONTENT_BYTES
    var readTimeoutMs: Long = DEFAULT_MAX_READ_TIMEOUT_MS
    var connectionTimeoutMs: Long = DEFAULT_MAX_CONNECTION_TIMEOUT_MS
    var writeTimeoutMs: Long = DEFAULT_MAX_WRITE_TIMEOUT_MS

    open var sentUrlString: String = EMPTY_URL
        protected set
    override var urlString: String = EMPTY_URL
        protected set
    override var state: State = State.NOT_SEND
        protected set
    override var body: ByteArray? = null
        protected set
    override var bodyAsString: String? = null
        protected set
        get() {
            return String(body ?: return null)
        }

    open fun send(url: String?): State {
        urlString = url ?: urlString
        val httpUrl = parseUrl(url ?: sentUrlString)

        state = if (httpUrl == null) {
            State.BAD_URL
        } else {
            val client = createClient(getClientBuilder())
            val request = createRequest(httpUrl)

            try {
                send(client, request)
            } catch (ex: Exception) {
                State.BAD_URL
            }
        }

        return state
    }

    private fun send(client: OkHttpClient, request: Request): State {
        return try {
            client.newCall(request).execute().use { response ->

                if (response.code() != OK_RESPONSE_CODE) {
                    return State.BAD_CONNECTION
                }

                response.body().use { body ->
                    when {
                        (body == null || body.contentLength() > maxContentBytes) ->
                            State.BAD_CONNECTION
                        else -> {
                            this.body = body.source().readByteArray()
                            State.SUCCESS
                        }
                    }
                }
            }
        } catch (ex: SecurityException) {
            State.PERMISSION_DENIED
        } catch (ex: Exception) {
            State.BAD_CONNECTION
        }
    }

    protected open fun createClient(clientBuilder: OkHttpClient.Builder): OkHttpClient {
        return clientBuilder.build()
    }

    protected abstract fun createRequest(url: HttpUrl): Request

    private fun getClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
                .readTimeout(readTimeoutMs, TimeUnit.MILLISECONDS)
                .connectTimeout(connectionTimeoutMs, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeoutMs, TimeUnit.MILLISECONDS)
    }

    private fun parseUrl(url: String): HttpUrl? {
        sentUrlString = url.trim()
                .replace('\\', '/')

        val result = HttpUrl.parse(sentUrlString)
                ?: HttpUrl.parse(DEFAULT_PROTOCOL + sentUrlString)

        sentUrlString = result?.toString() ?: sentUrlString

        if (!sentUrlString.isEmpty() && sentUrlString.last() == '/') {
            sentUrlString = sentUrlString.dropLast(1)
        }

        return result
    }
}
