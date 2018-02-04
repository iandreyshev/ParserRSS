package ru.iandreyshev.parserrss.models.useCase

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.ParserEngine
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult
import ru.iandreyshev.parserrss.presentation.presenter.IPresenter

abstract class DownloadRssUseCase(
        private val mRequestHandler: IHttpRequestHandler,
        private val mArticlesFilter: IArticlesFilter,
        private val mPresenter: IListener) : IUseCase {

    companion object {
        private const val MAX_ARTICLES_COUNT = 64
    }

    interface IListener : IPresenter {
        fun onInvalidUrl()

        fun onNetError(requestResult: IHttpRequestResult)

        fun onParserError()
    }

    open fun onUrlError() = mPresenter.onInvalidUrl()

    open fun onNetError(requestResult: IHttpRequestResult) = mPresenter.onNetError(requestResult)

    open fun onParserError() = mPresenter.onParserError()

    open fun isUrlValidAsync(): Boolean {
        return mRequestHandler.state != HttpRequestHandler.State.BAD_URL
    }

    open fun getRssFromNetAsync(): Boolean {
        mRequestHandler.sendGet()

        return mRequestHandler.state == HttpRequestHandler.State.SUCCESS
    }

    open fun parseRssAsync(): Rss? {
        return ParserEngine.parse(mRequestHandler.bodyAsString, MAX_ARTICLES_COUNT)
    }

    final override fun start() {
        mPresenter.processStart()
        doAsync {
            if (!isUrlValidAsync()) {
                uiThread {
                    mPresenter.processEnd()
                    onUrlError()
                }

                return@doAsync

            } else if (!getRssFromNetAsync()) {
                uiThread {
                    mPresenter.processEnd()
                    onNetError(mRequestHandler)
                }

                return@doAsync
            }

            parseRssAsync().let {
                when (it) {
                    null -> uiThread {
                        onParserError()
                    }
                    else -> {
                        mArticlesFilter.sort(it.articles)
                        onSuccessAsync(it)
                    }
                }
            }

            uiThread {
                mPresenter.processEnd()
            }
        }
    }

    protected abstract fun onSuccessAsync(rss: Rss)
}
