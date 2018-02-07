package ru.iandreyshev.parserrss.factory.httpRequestHandler

import ru.iandreyshev.parserrss.models.web.HttpRequestHandler

interface IHttpRequestHandlerFactory {
    fun create(type: HttpRequestHandlerType, url: String = ""): HttpRequestHandler
}
