package ru.iandreyshev.parserrss.interactor

import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.factory.useCase.IUseCaseFactory
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.factory.useCase.UseCaseType
import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener

class FeedInteractor(
        private val mUseCaseFactory: IUseCaseFactory,
        private val mListener: IUseCaseListener) {

    init {
        mUseCaseFactory
                .create(UseCaseType.LOAD_ALL_RSS, mListener)
                .start()
    }

    fun onAddNewRss(url: String) {
        mUseCaseFactory
                .create(UseCaseType.INSERT_RSS, mListener, url)
                .start()
    }

    fun onDeleteRss(rssId: Long?) {
        when (rssId) {
            null -> mUseCaseFactory
                    .create(UseCaseType.MESSAGE, mListener, App.getStr(R.string.feed_deleting_error))
            else -> mUseCaseFactory
                    .create(UseCaseType.DELETE_RSS, mListener, rssId)
        }.start()
    }

    fun onOpenRssInfo(rss: ViewRss?) {
        when (rss) {
            null -> mUseCaseFactory
                    .create(UseCaseType.MESSAGE, mListener, App.getStr(R.string.feed_open_info_error))
            else -> mUseCaseFactory
                    .create(UseCaseType.OPEN_RSS_INFO, mListener, rss)
        }.start()
    }

    fun onOpenArticle(articleId: Long) {
        mUseCaseFactory
                .create(UseCaseType.OPEN_ARTICLE, mListener, articleId)
                .start()
    }
}
