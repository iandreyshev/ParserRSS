package ru.iandreyshev.parserrss.models.useCase.rssInfo

import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener
import ru.iandreyshev.parserrss.models.useCase.UseCase
import ru.iandreyshev.parserrss.models.rss.Rss

class LoadRssInfoUseCase(
        private val mRss: Rss?,
        private val mListener: IListener) : UseCase(mListener) {

    interface IListener : IUseCaseListener {
        fun loadData(title: String?, description: String?)

        fun enableOpenOriginalButton(isEnabled: Boolean)
    }

    override fun onProcess() {
        mListener.loadData(mRss?.title, mRss?.description)
        mListener.enableOpenOriginalButton(mRss?.originUrl != null)
    }
}
