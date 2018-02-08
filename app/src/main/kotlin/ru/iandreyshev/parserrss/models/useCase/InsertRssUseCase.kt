package ru.iandreyshev.parserrss.models.useCase

import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.RssParser
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult

class InsertRssUseCase(
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
        fun urlToAddRssIsEmpty()

        fun rssAlreadyExist()

        fun rssCountIsMax()

        fun connectionError(requestResult: IHttpRequestResult)

        fun invalidRssFormat()

        fun insertNewRss(rss: ViewRss, isFull: Boolean)
    }

    private var mResultEvent: (() -> Unit)? = null
    private var mIsUrlEmpty: Boolean = false

    override fun isUrlValidAsync(url: String): Boolean {
        if (url.trim().isEmpty()) {
            mIsUrlEmpty = true

            return false
        }

        return !mRepository.isRssWithUrlExist(url)
    }

    override fun onStartProcessAsync(): Boolean {
        return if (mRepository.isFull) {
            mResultEvent = mListener::rssCountIsMax
            false
        } else {
            true
        }
    }

    override fun onUrlErrorAsync() {
        mResultEvent = if (mIsUrlEmpty) {
            mListener::urlToAddRssIsEmpty
        } else
            mListener::rssAlreadyExist
    }

    override fun onConnectionErrorAsync(requestResult: IHttpRequestResult) {
        mResultEvent = { mListener.connectionError(requestResult) }
    }

    override fun onParserErrorAsync() {
        mResultEvent = mListener::invalidRssFormat
    }

    override fun onSuccessAsync(rss: Rss) {
        mResultEvent = when (mRepository.putNewRss(rss)) {
            IRepository.InsertRssResult.SUCCESS -> {
                mArticlesFilter.sort(rss.articles)
                fun() { mListener.insertNewRss(ViewRss(rss), mRepository.isFull) }
            }
            IRepository.InsertRssResult.EXIST -> mListener::rssAlreadyExist
            IRepository.InsertRssResult.FULL -> mListener::rssCountIsMax
        }
    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)
        mResultEvent?.invoke()
    }
}
