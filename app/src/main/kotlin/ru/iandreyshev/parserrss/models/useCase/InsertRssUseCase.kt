package ru.iandreyshev.parserrss.models.useCase

import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.models.web.IHttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult

class InsertRssUseCase(
        private val mRepository: IRepository,
        private val mRequestHandler: IHttpRequestHandler,
        private val nArticlesFilter: IArticlesFilter,
        private val mPresenter: IListener) : DownloadRssUseCase(mRequestHandler, mPresenter) {

    private var mResultEvent: (() -> Unit)? = null

    interface IListener : IUseCaseListener {
        fun rssAlreadyExist()

        fun rssCountIsMax()

        fun invalidRssUrl()

        fun connectionError(requestResult: IHttpRequestResult)

        fun invalidRssFormat()

        fun insertNewRss(rss: ViewRss, isFull: Boolean)
    }

    override fun doInBackground(vararg params: Any?): Any? {
        return if (mRepository.isFull) {
            mResultEvent = mPresenter::rssCountIsMax
            null
        } else {
            super.doInBackground(*params)
        }
    }

    override fun isUrlValidAsync(): Boolean {
        if (!super.isUrlValidAsync()) {
            mResultEvent = mPresenter::invalidRssUrl
            return false

        } else if (mRepository.isRssWithUrlExist(mRequestHandler.urlString)) {
            mResultEvent = mPresenter::rssAlreadyExist
            return false
        }

        return true
    }

    override fun onNetErrorAsync(requestResult: IHttpRequestResult) {
        mResultEvent = { mPresenter.connectionError(requestResult) }
    }

    override fun onParserErrorAsync() {
        mResultEvent = mPresenter::invalidRssFormat
    }

    override fun onSuccessAsync(rss: Rss) {
        when (mRepository.putNewRss(rss)) {
            IRepository.PutRssState.SUCCESS -> {
                nArticlesFilter.sort(rss.articles)
                mResultEvent = { -> mPresenter.insertNewRss(ViewRss(rss), mRepository.isFull) }
            }
            IRepository.PutRssState.EXIST -> mResultEvent = mPresenter::rssAlreadyExist
            IRepository.PutRssState.FULL -> mResultEvent = mPresenter::rssCountIsMax
        }
    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)
        mResultEvent?.invoke()
    }
}
