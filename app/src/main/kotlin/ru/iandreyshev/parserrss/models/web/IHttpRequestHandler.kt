package ru.iandreyshev.parserrss.models.web

interface IHttpRequestHandler : IHttpRequestResult {
    var maxContentBytes: Long
    var readTimeoutMs: Long
    var connectionTimeoutMs: Long
    var writeTimeoutMs: Long

    fun sendGet(): HttpRequestHandler.State

    fun sendGet(url: String): HttpRequestHandler.State
}
