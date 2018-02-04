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

class GetArticleImageUseCase(
        private val mRepository: IRepository,
        private val mRequestHandler: IHttpRequestHandler,
        private val mArticleId: Long,
        private val mImageProps: IImageProps,
        private val mListener: IListener) : IUseCase {

    interface IListener {
        fun insertImage(imageBitmap: Bitmap)
    }

    override fun start() {
        doAsync {
            val article = mRepository.getArticleById(mArticleId)
            val articleImage = mRepository.getArticleImageByArticle(mArticleId)

            articleImage?.bitmap?.let {
                uiThread {
                    mListener.insertImage(mImageProps.configureToView(articleImage.bitmap
                            ?: return@uiThread))
                }
            }

            val imageUrl = article?.imageUrl ?: return@doAsync

            val requestResult = mRequestHandler.sendGet(imageUrl)
            val imageBytes = mRequestHandler.body
            var imageBitmap = imageBytes?.bitmap

            if (requestResult === HttpRequestHandler.State.SUCCESS && imageBitmap != null) {
                App.repository.putArticleImage(mArticleId, mImageProps.configureToMemory(imageBitmap))
                imageBitmap = mImageProps.configureToView(imageBitmap)
            }

            uiThread {
                mListener.insertImage(imageBitmap ?: return@uiThread)
            }
        }
    }
}
