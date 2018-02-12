package ru.iandreyshev.parserrss.models.useCase

import ru.iandreyshev.parserrss.models.parser.RssParser
import ru.iandreyshev.parserrss.models.rss.Rss
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult

abstract class DownloadRssUseCase(
        private val mRequestHandler: HttpRequestHandler,
        private val mParser: RssParser,
        mListener: IUseCaseListener) : UseCase(mListener) {

    private var mUrl: String = ""

    protected abstract fun getRssUrl(): String?

    protected abstract fun onUrlError()

    protected abstract fun onConnectionError(requestResult: IHttpRequestResult)

    protected abstract fun onParserError()

    protected abstract fun onSuccess(rss: Rss)

    protected open fun isUrlValid(url: String): Boolean {
        return true
    }

    protected open fun getRssFromNet(): Boolean {
        return mRequestHandler.send(mUrl) == HttpRequestHandler.State.SUCCESS
    }

    protected open fun parseRss(): Rss? {
        return mParser.parse(mRequestHandler.bodyAsString)
    }

    final override fun onProcess() {
        mUrl = getRssUrl() ?: mUrl

        if (!isUrlValid(mUrl)) {
            onUrlError()

            return

        } else if (!getRssFromNet()) {
            onConnectionError(mRequestHandler)

            return
        }

        parseRss().let { rss ->
            when (rss) {
                null -> onParserError()
                else -> {
                    rss.url = mRequestHandler.sentUrlString
                    onSuccess(rss)
                }
            }
        }
    }
}
