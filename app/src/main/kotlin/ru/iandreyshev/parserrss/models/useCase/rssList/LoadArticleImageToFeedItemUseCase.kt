package ru.iandreyshev.parserrss.models.useCase.rssList

import ru.iandreyshev.parserrss.models.imageProps.IImageProperties
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.useCase.GetArticleImageUseCase
import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.ui.adapter.IItemIcon

class LoadArticleImageToFeedItemUseCase(
        repository: IRepository,
        requestHandler: HttpRequestHandler,
        imageProps: IImageProperties,
        private val mIcon: IItemIcon,
        private val mListener: IListener)
    : GetArticleImageUseCase(
        repository,
        requestHandler,
        imageProps,
        mIcon.id,
        mListener) {

    interface IListener : IUseCaseListener {
        fun insertImage(icon: IItemIcon, idOnStart: Long, imageBytes: ByteArray)
    }

    private val mIdOnStart = mIcon.id

    override fun onFoundImage(imageBitmap: ByteArray) {
        mListener.insertImage(mIcon, mIdOnStart, imageBitmap)
    }

    override fun isProcessNotifyAvailable(): Boolean {
        return false
    }
}
