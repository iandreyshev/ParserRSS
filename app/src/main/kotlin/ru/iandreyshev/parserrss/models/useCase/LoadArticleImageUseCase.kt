package ru.iandreyshev.parserrss.models.useCase

import android.graphics.Bitmap

import ru.iandreyshev.parserrss.models.imageProps.IImageProps
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler

open class LoadArticleImageUseCase(
        repository: IRepository,
        requestHandler: HttpRequestHandler,
        imageProps: IImageProps,
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

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        if (result != null) {
            mListener.insertImage(result)
        }
    }
}
