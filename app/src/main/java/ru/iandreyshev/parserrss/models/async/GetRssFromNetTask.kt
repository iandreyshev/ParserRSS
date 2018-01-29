package ru.iandreyshev.parserrss.models.async

import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.ParserEngine
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult

abstract class GetRssFromNetTask(
        protected open val listener: IEventListener,
        protected val url: String) : Task<String, Void, Rss>(listener) {

    companion object {
        private const val MAX_ARTICLES_COUNT = 64
    }

    private var requestHandler: HttpRequestHandler = HttpRequestHandler(url)
    private var resultRss: Rss? = null

    open fun isUrlValid(): Boolean {
        return when (requestHandler.state != HttpRequestHandler.State.BadUrl) {
            true -> true
            false -> {
                setResultEvent { listener.onInvalidUrl() }
                false
            }
        }
    }

    open fun getRssFromNet(): Boolean {
        requestHandler.sendGet()

        return when (requestHandler.state == HttpRequestHandler.State.Success) {
            true -> true
            false -> {
                setResultEvent { listener.onNetError(requestHandler) }
                false
            }
        }
    }

    open fun parseRss(): Boolean {
        val rss = ParserEngine.parse(requestHandler.bodyAsString, MAX_ARTICLES_COUNT)

        return when (rss) {
            null -> {
                setResultEvent { listener.onParserError() }
                false
            }
            else -> {
                rss.url = requestHandler.urlString
                resultRss = rss
                true
            }
        }
    }

    override fun doInBackground(vararg args: String): Rss? {
        if (!isUrlValid() || !getRssFromNet() || !parseRss()) {
            return null
        }

        onSuccess(resultRss ?: return null)

        return resultRss
    }

    protected abstract fun onSuccess(rss: Rss)

    interface IEventListener : ITaskListener<String, Void, Rss> {
        fun onInvalidUrl()

        fun onNetError(requestResult: IHttpRequestResult)

        fun onParserError()
    }
}
