package ru.iandreyshev.parserrss.models.useCase

import android.graphics.Bitmap
import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.extention.bitmap
import ru.iandreyshev.parserrss.models.imageProps.IImageProps
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestHandler

abstract class GetArticleImageUseCase(
        private val mRepository: IRepository,
        private val mRequestHandler: IHttpRequestHandler,
        private val mImageProps: IImageProps,
        private val mArticleId: Long,
        mListener: IUseCaseListener) : BaseUseCase<Any, Any, Bitmap?>(mListener) {

    final override fun doInBackground(vararg params: Any?): Bitmap? {
        val bitmapFromRepo = mRepository.getArticleImageBitmap(mArticleId)

        if (bitmapFromRepo != null) {
            return bitmapFromRepo
        }

        val imageUrl = mRepository.getArticleImageUrl(mArticleId) ?: return null
        val requestResult = mRequestHandler.sendGet(imageUrl)
        var imageBitmap = mRequestHandler.body?.bitmap

        if (requestResult == HttpRequestHandler.State.SUCCESS && imageBitmap != null) {
            App.repository.putArticleImageIfArticleExist(mArticleId, mImageProps.configureToMemory(imageBitmap))
            imageBitmap = mImageProps.configureToView(imageBitmap)
        }

        return imageBitmap
    }
}
