package ru.iandreyshev.parserrss.models.useCase.feed

import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener
import ru.iandreyshev.parserrss.models.useCase.UseCase

class DeleteRssUseCase(
        private val mRepository: IRepository,
        private val mRssId: Long?,
        private val mListener: IListener) : UseCase(mListener) {

    interface IListener : IUseCaseListener {
        fun removeRss(id: Long)

        fun removingRssFailed()
    }

    override fun onProcess() {
        if (mRssId == null) {
            mListener.removingRssFailed()
            return
        }

        mRepository.runInTx {
            if (mRepository.removeRssById(mRssId)) {
                mListener.removeRss(mRssId)
            } else {
                mListener.removingRssFailed()
            }
        }
    }
}
