package ru.iandreyshev.parserrss.models.useCase

import android.graphics.Bitmap
import ru.iandreyshev.parserrss.models.imageProps.IImageProps
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.ui.adapter.IItemIcon

class LoadArticleImageToFeedItemUseCase(
        repository: IRepository,
        requestHandler: HttpRequestHandler,
        imageProps: IImageProps,
        private val mIcon: IItemIcon,
        mListener: IUseCaseListener)
    : GetArticleImageUseCase(
        repository,
        requestHandler,
        imageProps,
        mIcon.id,
        mListener) {

    private val mIdBeforeLoad = mIcon.id

    override fun start() {
        if (!mIcon.isUpdateStart) {
            mIcon.onStartUpdate()
            super.start()
        }
    }

    override fun onPreExecute() {
        // Implements to ignore notify listener about process starting
    }

    override fun onPostExecute(result: Bitmap?) {
        if (result != null && mIdBeforeLoad == mIcon.id) {
            mIcon.updateImage(result)
        }
    }
}
