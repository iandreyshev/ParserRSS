package ru.iandreyshev.parserrss.models.useCase.article

import android.graphics.Bitmap
import android.util.Log

import ru.iandreyshev.parserrss.models.imageProps.IImageProperties
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.useCase.GetArticleImageUseCase
import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler

open class LoadArticleImageUseCase(
        repository: IRepository,
        requestHandler: HttpRequestHandler,
        imageProps: IImageProperties,
        articleId: Long,
        private val mListener: IListener)
    : GetArticleImageUseCase(
        repository,
        requestHandler,
        imageProps,
        articleId,
        mListener) {

    interface IListener : IUseCaseListener {
        fun insertImage(imageBitmap: Bitmap)
    }

    override fun onFoundImage(imageBitmap: Bitmap) {
        mListener.insertImage(imageBitmap)
    }
}
