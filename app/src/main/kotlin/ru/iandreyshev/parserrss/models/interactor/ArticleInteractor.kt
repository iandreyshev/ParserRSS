package ru.iandreyshev.parserrss.models.interactor

import ru.iandreyshev.parserrss.factory.useCase.IUseCaseFactory
import ru.iandreyshev.parserrss.factory.useCase.UseCaseType
import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener

class ArticleInteractor(
        private val mUseCaseFactory: IUseCaseFactory,
        private val mListener: IUseCaseListener,
        private val mArticleId: Long) {

    init {
        mUseCaseFactory
                .create(UseCaseType.LOAD_ARTICLE, mListener, mArticleId)
                .start()

        mUseCaseFactory
                .create(UseCaseType.LOAD_ARTICLE_IMAGE, mListener, mArticleId)
                .start()
    }

    fun onOpenOriginal() {
        mUseCaseFactory
                .create(UseCaseType.OPEN_ARTICLE_ORIGINAL, mListener, mArticleId)
                .start()
    }
}
