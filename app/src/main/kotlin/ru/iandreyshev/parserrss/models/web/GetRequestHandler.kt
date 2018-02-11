package ru.iandreyshev.parserrss.models.web

import okhttp3.HttpUrl
import okhttp3.Request

class GetRequestHandler : HttpRequestHandler() {
    override fun createRequest(url: HttpUrl): Request {
        return Request.Builder()
                .url(url)
                .build()
    }
}
