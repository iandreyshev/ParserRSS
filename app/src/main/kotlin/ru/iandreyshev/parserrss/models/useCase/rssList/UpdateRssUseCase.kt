package ru.iandreyshev.parserrss.models.useCase.rssList

import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.parser.RssParser
import ru.iandreyshev.parserrss.models.useCase.DownloadRssUseCase
import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener
import ru.iandreyshev.parserrss.models.rss.Article
import ru.iandreyshev.parserrss.models.rss.Rss
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult

class UpdateRssUseCase(
        private val mRepository: IRepository,
        requestHandler: HttpRequestHandler,
        parser: RssParser,
        private val mRssId: Long,
        private val mArticlesFilter: IArticlesFilter,
        private val mListener: IListener)
    : DownloadRssUseCase(
        requestHandler,
        parser,
        mListener) {

    interface IListener : IUseCaseListener {
        fun connectionError(requestResult: IHttpRequestResult)

        fun parseError()

        fun rssNotExist()

        fun updateRss(articles: MutableList<Article>)
    }

    override fun getRssUrl(): String? {
        return mRepository.getRssById(mRssId)?.url
    }

    override fun isUrlValid(url: String): Boolean {
        return mRepository.isRssWithUrlExist(url)
    }

    override fun onUrlError() {
        mListener.rssNotExist()
    }

    override fun onConnectionError(requestResult: IHttpRequestResult) {
        mListener.connectionError(requestResult)
    }

    override fun onParserError() {
        mListener.parseError()
    }

    override fun onSuccess(rss: Rss) = mRepository.runInTx {
        if (mRepository.updateRssWithSameUrl(rss)) {
            mArticlesFilter.sort(rss.articles)
            mListener.updateRss(rss.articles)
        } else {
            mListener.rssNotExist()
        }
    }
}
