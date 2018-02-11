package ru.iandreyshev.parserrss.models.useCase

import android.graphics.Bitmap
import ru.iandreyshev.parserrss.models.extention.bitmap
import ru.iandreyshev.parserrss.models.imageProps.IImageProperties
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler

abstract class GetArticleImageUseCase(
        private val mRepository: IRepository,
        private val mRequestHandler: HttpRequestHandler,
        private val mProperties: IImageProperties,
        private val mArticleId: Long,
        mListener: IUseCaseListener) : UseCase(mListener) {

    protected abstract fun onFoundImage(imageBitmap: Bitmap)

    override fun onProcess() {
        val articleImage = mRepository.getArticleImageByArticleId(mArticleId)
        var imageBitmap = articleImage?.bitmap

        if (imageBitmap == null) {
            val imageUrl = mRepository.getArticleById(mArticleId)?.imageUrl ?: return
            val requestResult = mRequestHandler.send(imageUrl)
            imageBitmap = mRequestHandler.body?.bitmap

            if (requestResult == HttpRequestHandler.State.SUCCESS && imageBitmap != null) {
                imageBitmap = mProperties.configureToMemory(imageBitmap)
                mRepository.putArticleImageIfArticleExist(mArticleId, imageBitmap)
            }
        }

        onFoundImage(mProperties.configureToView(imageBitmap ?: return))
    }
}
