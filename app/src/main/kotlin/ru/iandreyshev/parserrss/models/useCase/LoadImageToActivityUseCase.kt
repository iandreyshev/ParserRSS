package ru.iandreyshev.parserrss.models.useCase

import android.graphics.Bitmap

import ru.iandreyshev.parserrss.models.imageProps.IImageProps
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.web.IHttpRequestHandler
import ru.iandreyshev.parserrss.presentation.presenter.IPresenter

open class LoadImageToActivityUseCase(
        repository: IRepository,
        requestHandler: IHttpRequestHandler,
        imageProps: IImageProps,
        articleId: Long,
        private val mListener: IListener)
    : LoadArticleImageUseCase(
        repository,
        requestHandler,
        imageProps,
        articleId) {

    interface IListener : IPresenter {
        fun insertImage(imageBitmap: Bitmap)
    }

    override fun start() {
        mListener.processStart()
        super.start()
        mListener.processEnd()
    }

    override fun onReady(imageBitmap: Bitmap) {
        mListener.insertImage(imageBitmap)
    }
}
