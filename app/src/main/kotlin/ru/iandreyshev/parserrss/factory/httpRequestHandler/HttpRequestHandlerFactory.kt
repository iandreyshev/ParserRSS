package ru.iandreyshev.parserrss.factory.httpRequestHandler

import ru.iandreyshev.parserrss.models.web.GetRequestHandler
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler

object HttpRequestHandlerFactory : IHttpRequestHandlerFactory {

    private const val MAX_IMAGE_BYTES_COUNT = 1048576L // 1MB
    private const val MAX_RSS_BYTES_COUNT = 5242880L // 5MB

    private object ArticleFeedItemHandlerProps {
        const val MAX_CONNECT_MS = 1500L
        const val MAX_READ_MS = 1500L
        const val MAX_WRITE_MS = 1500L
    }

    override fun create(type: HttpRequestHandlerType): HttpRequestHandler {
        return when (type) {
            HttpRequestHandlerType.ARTICLE_IMAGE -> {
                val handler = GetRequestHandler()
                handler.maxContentBytes = MAX_IMAGE_BYTES_COUNT

                handler
            }
            HttpRequestHandlerType.ARTICLE_FEED_ITEM_IMAGE -> {
                val handler = GetRequestHandler()
                handler.connectionTimeoutMs = ArticleFeedItemHandlerProps.MAX_CONNECT_MS
                handler.readTimeoutMs = ArticleFeedItemHandlerProps.MAX_READ_MS
                handler.writeTimeoutMs = ArticleFeedItemHandlerProps.MAX_WRITE_MS
                handler.maxContentBytes = MAX_IMAGE_BYTES_COUNT

                handler
            }
            HttpRequestHandlerType.NEW_RSS -> {
                val handler = GetRequestHandler()
                handler.maxContentBytes = MAX_RSS_BYTES_COUNT
                handler
            }
            HttpRequestHandlerType.UPDATE_RSS -> {
                val handler = GetRequestHandler()
                handler.maxContentBytes = MAX_RSS_BYTES_COUNT
                handler
            }
        }
    }
}
