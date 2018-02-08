package ru.iandreyshev.parserrss.models.useCase

import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.parser.RssParser
import ru.iandreyshev.parserrss.models.viewModels.ViewArticle
import ru.iandreyshev.parserrss.models.viewModels.ViewRss
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult

class UpdateRssUseCase(
        private val mRepository: IRepository,
        requestHandler: HttpRequestHandler,
        parser: RssParser,
        url: String,
        private val mArticlesFilter: IArticlesFilter,
        private val mListener: IListener)
    : DownloadRssUseCase(
        requestHandler,
        parser,
        url,
        mRepository.maxArticlesInRssCount,
        mListener) {

    interface IListener : IUseCaseListener {
        fun connectionError(requestResult: IHttpRequestResult)

        fun parseError()

        fun rssNotExist()

        fun updateRss(articles: MutableList<ViewArticle>)
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
            fun() { mListener.updateRss(ViewRss(rss).articles) }
        } else {
            mListener::rssNotExist
        }
    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)
        mResultEvent?.invoke()
    }
}
