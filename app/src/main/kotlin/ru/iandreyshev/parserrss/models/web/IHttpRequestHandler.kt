package ru.iandreyshev.parserrss.models.web

interface IHttpRequestHandler : IHttpRequestResult {
    var maxContentBytes: Long
    var readTimeoutSec: Long
    var connectionTimeoutSec: Long
    var writeTimeoutSec: Long

    fun sendGet(): HttpRequestHandler.State

    fun sendGet(url: String): HttpRequestHandler.State
}
