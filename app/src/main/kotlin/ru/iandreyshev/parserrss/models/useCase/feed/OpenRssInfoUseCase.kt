package ru.iandreyshev.parserrss.models.useCase.feed

import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener
import ru.iandreyshev.parserrss.models.useCase.UseCase
import ru.iandreyshev.parserrss.models.rss.Rss

class OpenRssInfoUseCase(
        private val mRss: Rss?,
        private val mListener: IListener) : UseCase(mListener) {

    interface IListener : IUseCaseListener {
        fun openRssInfo(rss: Rss?)
    }

    override fun onProcess() = mListener.openRssInfo(mRss)
}
