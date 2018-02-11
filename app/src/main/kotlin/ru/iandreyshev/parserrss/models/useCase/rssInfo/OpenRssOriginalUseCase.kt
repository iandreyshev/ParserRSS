package ru.iandreyshev.parserrss.models.useCase.rssInfo

import android.net.Uri
import ru.iandreyshev.parserrss.models.extention.uri
import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener
import ru.iandreyshev.parserrss.models.useCase.UseCase

class OpenRssOriginalUseCase(
        private val mOriginUrl: String?,
        private val mListener: IListener) : UseCase(mListener) {

    interface IListener : IUseCaseListener {
        fun openOriginal(path: Uri?)
    }

    override fun onProcess() = mListener.openOriginal(mOriginUrl?.uri)
}
