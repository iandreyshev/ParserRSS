package ru.iandreyshev.parserrss.models.useCase

import android.graphics.Bitmap
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.extention.bitmap
import ru.iandreyshev.parserrss.models.imageProps.IImageProps
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestHandler

abstract class LoadArticleImageUseCase(
        private val mRepository: IRepository,
        private val mRequestHandler: IHttpRequestHandler,
        private val mImageProps: IImageProps,
        private val mArticleId: Long) : IUseCase {

    override fun start() {
        doAsync {
            val bitmapFromRepo = mRepository.getArticleImageBitmap(mArticleId)

            bitmapFromRepo?.let {
                uiThread {
                    onReady(bitmapFromRepo)
                }
                return@doAsync
            }

            val imageUrl = mRepository.getArticleImageUrl(mArticleId) ?: return@doAsync

            mRequestHandler.maxContentBytes = mImageProps.maxBytesCount
            val requestResult = mRequestHandler.sendGet(imageUrl)
            var imageBitmap = mRequestHandler.body?.bitmap

            if (requestResult == HttpRequestHandler.State.SUCCESS && imageBitmap != null) {
                App.repository.putArticleImage(mArticleId, mImageProps.configureToMemory(imageBitmap))
                imageBitmap = mImageProps.configureToView(imageBitmap)
            }

            uiThread {
                onReady(imageBitmap ?: return@uiThread)
            }
        }
    }

    open fun onReady(imageBitmap: Bitmap) {
        // Impl to handle bitmap loading
    }
}
