package ru.iandreyshev.parserrss.models.useCase

import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.RssParser
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult

class UpdateRssUseCase(
        private val mRepository: IRepository,
        requestHandler: HttpRequestHandler,
        url: String,
        parser: RssParser,
        private val mArticlesFilter: IArticlesFilter,
        private val mListener: IListener)
    : DownloadRssUseCase(
        requestHandler,
        url,
        parser,
        mRepository.maxArticlesInRss,
        mListener) {

    interface IListener : IUseCaseListener {
        fun connectionError(requestResult: IHttpRequestResult)

        fun parseError()

        fun rssNotExist()

        fun updateSuccess(articles: MutableList<ViewArticle>)
    }

    private var mResultEvent: (() -> Unit)? = null

    override fun isUrlValidAsync(url: String): Boolean {
        return mRepository.isRssWithUrlExist(url)
    }

    override fun onUrlErrorAsync() {
        mResultEvent = mListener::rssNotExist
    }

    override fun onConnectionErrorAsync(requestResult: IHttpRequestResult) {
        mResultEvent = { mListener.connectionError(requestResult) }
    }

    override fun onParserErrorAsync() {
        mResultEvent = mListener::parseError
    }

    override fun onSuccessAsync(rss: Rss) {
        mResultEvent = if (mRepository.updateRssWithSameUrl(rss)) {
            mArticlesFilter.sort(rss.articles)
            fun() { mListener.updateSuccess(ViewRss(rss).articles) }
        } else {
            mListener::rssNotExist
        }
    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)
        mResultEvent?.invoke()
    }
}
