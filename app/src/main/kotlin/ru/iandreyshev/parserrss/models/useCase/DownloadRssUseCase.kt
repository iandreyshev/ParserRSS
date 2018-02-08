package ru.iandreyshev.parserrss.models.useCase

import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.ParserEngine
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult

abstract class DownloadRssUseCase(
        private val mRequestHandler: HttpRequestHandler,
        private var mUrl: String,
        mListener: IUseCaseListener) : BaseUseCase<Any, Any, Any?>(mListener) {

    companion object {
        private const val MAX_ARTICLES_COUNT = 64
    }

    protected abstract fun onUrlErrorAsync()

    protected abstract fun onNetErrorAsync(requestResult: IHttpRequestResult)

    protected abstract fun onParserErrorAsync()

    protected open fun isUrlValidAsync(url: String): Boolean {
        return true
    }

    protected open fun getRssFromNetAsync(): Boolean {
        return mRequestHandler.send(mUrl) == HttpRequestHandler.State.SUCCESS
    }

    protected open fun parseRssAsync(): Rss? {
        return ParserEngine.parse(mRequestHandler.bodyAsString, MAX_ARTICLES_COUNT)
    }

    override fun doInBackground(vararg params: Any?): Any? {
        if (!isUrlValidAsync(mUrl)) {
            onUrlErrorAsync()

            return null

        } else if (!getRssFromNetAsync()) {
            onNetErrorAsync(mRequestHandler)

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

    protected abstract fun onSuccessAsync(rss: Rss)
}
