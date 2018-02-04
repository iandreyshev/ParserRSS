package ru.iandreyshev.parserrss.models.async

import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.ParserEngine
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult

abstract class GetRssFromNetTask(
        private val mListener: IEventListener,
        url: String) : Task<String, Void, Rss>(mListener) {

    companion object {
        private const val MAX_ARTICLES_COUNT = 64
        private const val MAX_CONNECTION_TIMEOUT_SEC = 4L
        private const val MAX_READ_TIMEOUT_SEC = 4L
        private const val MAX_WRITE_TIMEOUT_SEC = 4L
    }

    private var requestHandler: HttpRequestHandler = HttpRequestHandler(url)
    private var resultRss: Rss? = null

    open fun isUrlValid(): Boolean {
        return when (requestHandler.state != HttpRequestHandler.State.BAD_URL) {
            true -> true
            false -> {
                setResultEvent { mListener.onInvalidUrl() }
                false
            }
        }
    }

    open fun getRssFromNet(): Boolean {
        requestHandler.connectionTimeoutSec = MAX_CONNECTION_TIMEOUT_SEC
        requestHandler.readTimeoutSec = MAX_READ_TIMEOUT_SEC
        requestHandler.writeTimeoutSec = MAX_WRITE_TIMEOUT_SEC
        requestHandler.sendGet()

        return when (requestHandler.state == HttpRequestHandler.State.SUCCESS) {
            true -> true
            false -> {
                setResultEvent { mListener.onNetError(requestHandler) }
                false
            }
        }
    }

    open fun parseRss(): Boolean {
        val rss = ParserEngine.parse(requestHandler.bodyAsString, MAX_ARTICLES_COUNT)

        return when (rss) {
            null -> {
                setResultEvent { mListener.onParserError() }
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
