package ru.iandreyshev.parserrss.models.useCase

import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult

class InsertRssUseCase(
        private val mRepository: IRepository,
        requestHandler: HttpRequestHandler,
        url: String,
        private val mArticlesFilter: IArticlesFilter,
        private val mListener: IListener) : DownloadRssUseCase(requestHandler, url, mListener) {

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

    override fun doInBackground(vararg params: Any?): Any? {
        return if (mRepository.isFull) {
            mResultEvent = mListener::rssCountIsMax
            null
        } else {
            super.doInBackground(*params)
        }
    }

    override fun onUrlErrorAsync() {
        mResultEvent = if (mIsUrlEmpty) {
            mListener::urlToAddRssIsEmpty
        } else
            mListener::rssAlreadyExist
    }

    override fun onNetErrorAsync(requestResult: IHttpRequestResult) {
        mResultEvent = { mListener.connectionError(requestResult) }
    }

    override fun onParserErrorAsync() {
        mResultEvent = mListener::invalidRssFormat
    }

    override fun onSuccessAsync(rss: Rss) {
        mResultEvent = when (mRepository.putNewRss(rss)) {
            IRepository.PutRssState.SUCCESS -> {
                mArticlesFilter.sort(rss.articles)
                fun() { mListener.insertNewRss(ViewRss(rss), mRepository.isFull) }
            }
            IRepository.PutRssState.EXIST -> mListener::rssAlreadyExist
            IRepository.PutRssState.FULL -> mListener::rssCountIsMax
        }
    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)
        mResultEvent?.invoke()
    }
}
