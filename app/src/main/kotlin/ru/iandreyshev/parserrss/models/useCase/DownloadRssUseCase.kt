package ru.iandreyshev.parserrss.models.useCase

import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.ParserEngine
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult

abstract class DownloadRssUseCase(
        private val mRequestHandler: IHttpRequestHandler,
        private val mArticlesFilter: IArticlesFilter,
        mListener: IUseCaseListener) : BaseUseCase<Any, Any, Any?>(mListener) {

    companion object {
        private const val MAX_ARTICLES_COUNT = 64
    }

    protected open fun onUrlErrorAsync() {
        // Implement in sub-classes if needed
    }

    protected open fun onNetErrorAsync(requestResult: IHttpRequestResult) {
        // Implement in sub-classes if needed
    }

    protected open fun onParserErrorAsync() {
        // Implement in sub-classes if needed
    }

    protected open fun isUrlValidAsync(): Boolean {
        return mRequestHandler.state != HttpRequestHandler.State.BAD_URL
    }

    protected open fun getRssFromNetAsync(): Boolean {
        mRequestHandler.sendGet()

        return mRequestHandler.state == HttpRequestHandler.State.SUCCESS
    }

    protected open fun parseRssAsync(): Rss? {
        return ParserEngine.parse(mRequestHandler.bodyAsString, MAX_ARTICLES_COUNT)
    }

    override fun doInBackground(vararg params: Any?): Any? {
        if (!isUrlValidAsync()) {
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
                    mArticlesFilter.sort(it.articles)
                    onSuccessAsync(it)
                }
            }
        }

        return null
    }

    protected abstract fun onSuccessAsync(rss: Rss)
}
