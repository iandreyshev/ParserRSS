package ru.iandreyshev.parserrss.models.web

import okhttp3.HttpUrl
import okhttp3.Request

class GetRequestHandler(urlString: String = DEFAULT_URL) : HttpRequestHandler(urlString) {

    companion object {
        private const val DEFAULT_URL = ""
    }

    override fun createRequest(url: HttpUrl): Request {
        return Request.Builder()
                .url(url)
                .build()
    }
}
