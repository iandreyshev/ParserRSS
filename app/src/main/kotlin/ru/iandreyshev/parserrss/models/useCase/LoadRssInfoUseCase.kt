package ru.iandreyshev.parserrss.models.useCase

import ru.iandreyshev.parserrss.models.rss.ViewRss

class LoadRssInfoUseCase(
        private val mRss: ViewRss,
        private val mPresenter: IListener) : IUseCase {

    interface IListener {
        fun setInfo(title: String?, description: String?)

        fun enableOpenOriginalButton(isEnabled: Boolean)
    }

    override fun start() {
        mPresenter.setInfo(mRss.title, mRss.description)
        mPresenter.enableOpenOriginalButton(mRss.origin != null)
    }
}
