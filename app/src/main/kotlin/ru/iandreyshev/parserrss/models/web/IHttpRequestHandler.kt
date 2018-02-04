package ru.iandreyshev.parserrss.models.web

interface IHttpRequestHandler : IHttpRequestResult {
    fun sendGet(): HttpRequestHandler.State

    fun sendGet(url: String): HttpRequestHandler.State
}
