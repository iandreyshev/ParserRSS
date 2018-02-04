package ru.iandreyshev.parserrss.interactor

import ru.iandreyshev.parserrss.factory.IUseCaseFactory
import ru.iandreyshev.parserrss.models.useCase.*
import ru.iandreyshev.parserrss.presentation.presenter.IPresenter
import ru.iandreyshev.parserrss.ui.adapter.IItemIcon
import ru.iandreyshev.parserrss.models.rss.ViewRss

class FeedPageInteractor(
        private val mUseCaseFactory: IUseCaseFactory,
        private val mPresenter: IListener,
        private val mRss: ViewRss) {

    interface IListener : IPresenter,
            UpdateRssUseCase.IListener,
            LoadArticlesFirstTimeUseCase.IListener

    init {
        mUseCaseFactory.create(
                UseCaseType.LOAD_ARTICLES_FIRST_TIME,
                mPresenter,
                mRss
        ).start()
    }

    fun load(icon: IItemIcon) {
        mUseCaseFactory.create(
                UseCaseType.LOAD_IMAGE_TO_FEED_ITEM,
                mPresenter,
                icon
        ).start()
    }

    fun onUpdate() {
        mUseCaseFactory.create(
                UseCaseType.UPDATE_RSS,
                mPresenter,
                mRss.id
        ).start()
    }
}
