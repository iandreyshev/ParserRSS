package ru.iandreyshev.parserrss.factory.httpRequestHandler

import ru.iandreyshev.parserrss.models.web.IHttpRequestHandler

interface IHttpRequestHandlerFactory {
    fun create(type: HttpRequestHandlerType, url: String = ""): IHttpRequestHandler
}
