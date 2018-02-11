package ru.iandreyshev.parserrss.models.useCase.feed

import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.parser.RssParser
import ru.iandreyshev.parserrss.models.useCase.DownloadRssUseCase
import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener
import ru.iandreyshev.parserrss.models.viewModels.ViewRss
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult

class InsertRssUseCase(
        private val mRepository: IRepository,
        requestHandler: HttpRequestHandler,
        parser: RssParser,
        private val mUrl: String,
        private val mListener: IListener)
    : DownloadRssUseCase(
        requestHandler,
        parser,
        mListener) {

    interface IListener : IUseCaseListener {
        fun urlToAddRssIsEmpty()

        fun rssAlreadyExist()

        fun rssCountIsMax()

        fun connectionError(requestResult: IHttpRequestResult)

        fun invalidRssFormat(url: String)

        fun insertNewRss(rss: ViewRss, isFull: Boolean)
    }

    override fun getRssUrl(): String? {
        return mUrl
    }

    override fun isStartProcessAvailable(): Boolean {
        return if (mRepository.isFull) {
            mListener.rssCountIsMax()
            false
        } else {
            true
        }
    }

    override fun isUrlValid(url: String): Boolean {
        if (url.trim().isEmpty()) {
            mListener.urlToAddRssIsEmpty()

            return false

        } else if (mRepository.isRssWithUrlExist(url)) {
            mListener.rssAlreadyExist()

            return false
        }

        return true
    }

    override fun onUrlError() {
    }

    override fun onConnectionError(requestResult: IHttpRequestResult) {
        mListener.connectionError(requestResult)
    }

    override fun onParserError() {
        mListener.invalidRssFormat(mUrl)
    }

    override fun onSuccess(rss: Rss) {
        when (mRepository.putNewRss(rss)) {
            IRepository.InsertRssResult.SUCCESS -> {
                mListener.insertNewRss(ViewRss(rss), mRepository.isFull)
            }
            IRepository.InsertRssResult.EXIST -> mListener.rssAlreadyExist()
            IRepository.InsertRssResult.FULL -> mListener.rssCountIsMax()
        }
    }
}
