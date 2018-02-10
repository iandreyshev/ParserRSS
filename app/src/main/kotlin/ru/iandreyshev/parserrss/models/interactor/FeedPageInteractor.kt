package ru.iandreyshev.parserrss.models.interactor

import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.factory.useCase.IUseCaseFactory
import ru.iandreyshev.parserrss.factory.useCase.UseCaseType
import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener
import ru.iandreyshev.parserrss.ui.adapter.IItemIcon
import ru.iandreyshev.parserrss.models.viewModels.ViewRss

class FeedPageInteractor(
        private val mUseCaseFactory: IUseCaseFactory,
        private val mListener: IUseCaseListener,
        private val mRss: ViewRss) {

    init {
        mUseCaseFactory
                .create(UseCaseType.LOAD_ARTICLES_FIRST_TIME, mListener, mRss.articles)
                .start()
    }

    fun updateImage(icon: IItemIcon) {
        mUseCaseFactory
                .create(UseCaseType.LOAD_ARTICLE_IMAGE_TO_FEED_ITEM, mListener, icon)
                .start()
    }

    fun onUpdate() {
        val url = mRss.url
        when (url) {
            null -> mUseCaseFactory
                    .create(UseCaseType.MESSAGE, mListener, App.getStr(R.string.toast_invalid_url))
            else -> mUseCaseFactory
                    .create(UseCaseType.UPDATE_RSS, mListener, url)
        }.start()
    }
}
