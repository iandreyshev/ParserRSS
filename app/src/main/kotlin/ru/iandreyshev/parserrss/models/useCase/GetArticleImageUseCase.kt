package ru.iandreyshev.parserrss.models.useCase

import android.graphics.Bitmap
import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.extention.bitmap
import ru.iandreyshev.parserrss.models.imageProps.IImageProps
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler

abstract class GetArticleImageUseCase(
        private val mRepository: IRepository,
        private val mRequestHandler: HttpRequestHandler,
        private val mImageProps: IImageProps,
        private val mArticleId: Long,
        mListener: IUseCaseListener) : BaseUseCase<Any, Any, Bitmap?>(mListener) {

    final override fun doInBackground(vararg params: Any?): Bitmap? {
        val bitmapFromRepo = mRepository.getArticleImageBitmapByArticleId(mArticleId)

        if (bitmapFromRepo != null) {
            return mImageProps.configureToView(bitmapFromRepo)
        }

        val imageUrl = mRepository.getArticleImageUrlByArticleId(mArticleId) ?: return null
        val requestResult = mRequestHandler.send(imageUrl)
        var imageBitmap = mRequestHandler.body?.bitmap

        if (requestResult == HttpRequestHandler.State.SUCCESS && imageBitmap != null) {
            App.repository.putArticleImageIfArticleExist(mArticleId, mImageProps.configureToMemory(imageBitmap))
            imageBitmap = mImageProps.configureToView(imageBitmap)
        }

        return imageBitmap
    }
}
