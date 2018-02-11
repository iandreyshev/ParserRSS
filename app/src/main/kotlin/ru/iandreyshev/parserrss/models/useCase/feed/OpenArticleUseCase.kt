package ru.iandreyshev.parserrss.models.useCase.feed

import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener
import ru.iandreyshev.parserrss.models.useCase.UseCase

class OpenArticleUseCase(
        private val mArticleId: Long,
        private val mListener: IListener) : UseCase(mListener) {

    interface IListener : IUseCaseListener {
        fun openArticle(articleId: Long)
    }

    override fun onProcess() = mListener.openArticle(mArticleId)
}
