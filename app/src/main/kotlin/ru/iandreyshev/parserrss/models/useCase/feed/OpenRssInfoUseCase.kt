package ru.iandreyshev.parserrss.models.useCase.feed

import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener
import ru.iandreyshev.parserrss.models.useCase.UseCase
import ru.iandreyshev.parserrss.models.viewModels.ViewRss

class OpenRssInfoUseCase(
        private val mRss: ViewRss?,
        private val mListener: IListener) : UseCase(mListener) {

    interface IListener : IUseCaseListener {
        fun openRssInfo(rss: ViewRss?)
    }

    override fun onProcess() = mListener.openRssInfo(mRss)
}
