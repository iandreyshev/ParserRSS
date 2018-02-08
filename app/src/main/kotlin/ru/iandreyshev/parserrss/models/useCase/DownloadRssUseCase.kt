package ru.iandreyshev.parserrss.models.useCase

import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.RssParser
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult

abstract class DownloadRssUseCase(
        private val mRequestHandler: HttpRequestHandler,
        private var mUrl: String,
        private val mParser: RssParser,
        private val mMaxArticlesCount: Int,
        mListener: IUseCaseListener) : BaseUseCase<Any, Any, Any?>(mListener) {

    protected abstract fun onUrlErrorAsync()

    protected abstract fun onConnectionErrorAsync(requestResult: IHttpRequestResult)

    protected abstract fun onParserErrorAsync()

    protected abstract fun onSuccessAsync(rss: Rss)

    protected open fun onStartProcessAsync(): Boolean {
        return true
    }

    protected open fun isUrlValidAsync(url: String): Boolean {
        return true
    }

    protected open fun getRssFromNetAsync(): Boolean {
        return mRequestHandler.send(mUrl) == HttpRequestHandler.State.SUCCESS
    }

    protected open fun parseRssAsync(): Rss? {
        return mParser.parse(mRequestHandler.bodyAsString, mMaxArticlesCount)
    }

    final override fun doInBackground(vararg params: Any?): Any? {
        if (!onStartProcessAsync()) {

            return null

        } else if (!isUrlValidAsync(mUrl)) {
            onUrlErrorAsync()

            return null

        } else if (!getRssFromNetAsync()) {
            onConnectionErrorAsync(mRequestHandler)

            return null
        }

        parseRssAsync().let {
            when (it) {
                null -> onParserErrorAsync()
                else -> {
                    it.url = mRequestHandler.urlString
                    onSuccessAsync(it)
                }
            }
        }

        return null
    }
}
