package ru.iandreyshev.parserrss.models.useCase

import ru.iandreyshev.parserrss.models.viewModels.ViewRss

class OpenRssInfoUseCase(
        private val mRss: ViewRss,
        private val mListener: IListener) : IUseCase {

    interface IListener {
        fun openRssInfo(rss: ViewRss)
    }

    override fun start() = mListener.openRssInfo(mRss)
}
