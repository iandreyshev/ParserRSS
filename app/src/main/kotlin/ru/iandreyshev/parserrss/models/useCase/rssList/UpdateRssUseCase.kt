package ru.iandreyshev.parserrss.models.useCase.rssList

import ru.iandreyshev.parserrss.models.extention.viewModel
import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.parser.RssParser
import ru.iandreyshev.parserrss.models.useCase.DownloadRssUseCase
import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener
import ru.iandreyshev.parserrss.models.viewModels.ViewArticle
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

        fun updateRss(articles: ArrayList<ViewArticle>)
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

    override fun onSuccess(rss: Rss) {
        if (mRepository.updateRssWithSameUrl(rss)) {
            mArticlesFilter.sort(rss.articles)
            val resultArticles = ArrayList(rss.articles.map { it.viewModel })
            mListener.updateRss(resultArticles)
        } else {
            mListener.rssNotExist()
        }
    }
}
