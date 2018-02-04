package ru.iandreyshev.parserrss.models.useCase

import android.net.Uri
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import ru.iandreyshev.parserrss.models.extention.uri
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.presentation.presenter.IPresenter

class OpenOriginalUseCase(
        private val mRepository: IRepository,
        private val mArticleId: Long,
        private val mListener: IListener) : IUseCase {

    interface IListener : IPresenter {
        fun openOriginal(path: Uri)

        fun openOriginalFail()
    }

    override fun start() {
        mListener.processStart()
        doAsync {
            val original = mRepository.getArticleById(mArticleId)?.originUrl
            val path = original?.uri

            uiThread {
                mListener.processEnd()
                when (path) {
                    null -> mListener.openOriginalFail()
                    else -> mListener.openOriginal(path)
                }
            }
        }
    }
}
