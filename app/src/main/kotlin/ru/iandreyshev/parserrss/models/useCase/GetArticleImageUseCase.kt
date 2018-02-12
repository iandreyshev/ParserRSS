package ru.iandreyshev.parserrss.models.useCase

import ru.iandreyshev.parserrss.models.imageProps.IImageProperties
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.rss.ArticleImage
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler

abstract class GetArticleImageUseCase(
        private val mRepository: IRepository,
        private val mRequestHandler: HttpRequestHandler,
        private val mProperties: IImageProperties,
        private val mArticleId: Long,
        mListener: IUseCaseListener) : UseCase(mListener) {

    protected abstract fun onFoundImage(imageBitmap: ByteArray)

    override fun onProcess() {
        val articleImage = mRepository.getArticleImageByArticleId(mArticleId)
        var imageBytes = articleImage?.bytes

        if (imageBytes == null) {
            val imageUrl = mRepository.getArticleById(mArticleId)?.imageUrl ?: return
            val requestResult = mRequestHandler.send(imageUrl)
            imageBytes = mRequestHandler.body

            if (requestResult == HttpRequestHandler.State.SUCCESS && imageBytes != null) {
                imageBytes = mProperties.configureToMemory(imageBytes)
                mRepository.putArticleImageIfArticleExist(ArticleImage(
                        articleId = mArticleId,
                        bytes = imageBytes
                ))
            }
        }

        onFoundImage(mProperties.configureToView(imageBytes ?: return))
    }
}
