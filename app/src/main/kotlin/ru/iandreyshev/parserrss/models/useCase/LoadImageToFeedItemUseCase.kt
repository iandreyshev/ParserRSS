package ru.iandreyshev.parserrss.models.useCase

import android.graphics.Bitmap
import ru.iandreyshev.parserrss.models.imageProps.IImageProps
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.web.IHttpRequestHandler
import ru.iandreyshev.parserrss.ui.adapter.IItemIcon

class LoadImageToFeedItemUseCase(
        repository: IRepository,
        requestHandler: IHttpRequestHandler,
        imageProps: IImageProps,
        private val mIcon: IItemIcon)
    : LoadArticleImageUseCase(
        repository,
        requestHandler,
        imageProps,
        mIcon.id) {

    private val mIdBeforeLoad = mIcon.id

    override fun onReady(imageBitmap: Bitmap) {
        if (mIdBeforeLoad == mIcon.id) {
            mIcon.updateImage(imageBitmap)
        }
    }

    override fun start() {
        if (!mIcon.isLoaded) {
            mIcon.isLoaded = true
            super.start()
        }
    }
}
