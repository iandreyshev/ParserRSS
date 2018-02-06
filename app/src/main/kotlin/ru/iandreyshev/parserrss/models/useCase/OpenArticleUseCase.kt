package ru.iandreyshev.parserrss.models.useCase

class OpenArticleUseCase(
        private val mArticleId: Long,
        private val mListener: IListener) : IUseCase {

    interface IListener {
        fun openArticle(articleId: Long)
    }

    override fun start() = mListener.openArticle(mArticleId)
}
