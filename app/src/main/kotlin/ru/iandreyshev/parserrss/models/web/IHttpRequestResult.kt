package ru.iandreyshev.parserrss.models.web

interface IHttpRequestResult {
    val state: HttpRequestHandler.State

    val urlString: String

    val body: ByteArray?

    val bodyAsString: String?
}
