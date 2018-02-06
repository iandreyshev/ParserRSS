package ru.iandreyshev.parserrss.models.useCase

import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.models.web.IHttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult

class UpdateRssUseCase(
        private val mRepository: IRepository,
        private val mRequestHandler: IHttpRequestHandler,
        private val mArticlesFilter: IArticlesFilter,
        private val mListener: IListener) : DownloadRssUseCase(mRequestHandler, mListener) {

    interface IListener : IUseCaseListener {
        fun invalidUrl()

        fun connectionError(requestResult: IHttpRequestResult)

        fun parseError()

        fun rssNotExist()

        fun updateSuccess(articles: MutableList<ViewArticle>)
    }

    private var mResultEvent: (() -> Unit)? = null

    override fun isUrlValidAsync(): Boolean {
        if (!super.isUrlValidAsync()) {
            mResultEvent = mListener::invalidUrl
            return false

        } else if (!mRepository.isRssWithUrlExist(mRequestHandler.urlString)) {
            mResultEvent = mListener::rssNotExist
            return false
        }

        return true
    }

    override fun onNetErrorAsync(requestResult: IHttpRequestResult) {
        mResultEvent = { mListener.connectionError(requestResult) }
    }

    override fun onParserErrorAsync() {
        mResultEvent = mListener::parseError
    }

    override fun onSuccessAsync(rss: Rss) {
        if (mRepository.updateRssWithSameUrl(rss)) {
            mArticlesFilter.sort(rss.articles)
            mResultEvent = { mListener.updateSuccess(ViewRss(rss).articles) }
        } else {
            mResultEvent = mListener::rssNotExist
        }
    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)
        mResultEvent?.invoke()
    }
}
