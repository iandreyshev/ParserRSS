package ru.iandreyshev.parserrss.models.web

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

abstract class HttpRequestHandler(urlString: String) : IHttpRequestResult {

    companion object {
        internal const val DEFAULT_MAX_CONTENT_BYTES = 5242880L // 5MB

        private const val OK_RESPONSE_CODE = 200
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

    final override var urlString = urlString
        private set
    final override var state: State = State.NOT_SEND
        private set
    final override var body: ByteArray? = null
        private set
    final override val bodyAsString: String?
        get() {
            return String(body ?: return null)
        }

    fun send(url: String? = null): State {
        url?.let { urlString = url }
        val httpUrl = parseUrl(url ?: urlString)

        state = if (httpUrl == null) {
            State.BAD_URL
        } else {
            val client = createClient(getClientBuilder())
            val request = createRequest(httpUrl)
            send(client, request)
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
        return HttpUrl.parse(url) ?: HttpUrl.parse(DEFAULT_PROTOCOL + url)
    }
}
