package ru.iandreyshev.parserrss.models.useCase

import android.net.Uri
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import ru.iandreyshev.parserrss.models.extention.uri
import ru.iandreyshev.parserrss.models.repository.IRepository

class OpenOriginalUseCase(
        private val mRepository: IRepository,
        private val mArticleId: Long,
        private val mListener: IListener) : IUseCase {

    interface IListener {
        fun openOriginal(path: Uri)

        fun openOriginalFail()
    }

    override fun start() {
        doAsync {
            val original = mRepository.getArticleById(mArticleId)?.originUrl
            val path = original?.uri

            when (path) {
                null -> uiThread {
                    mListener.openOriginalFail()
                }
                else -> uiThread {
                    mListener.openOriginal(path)
                }
            }
        }
    }
}
