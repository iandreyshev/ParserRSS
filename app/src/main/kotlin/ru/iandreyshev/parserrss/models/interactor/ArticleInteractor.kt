package ru.iandreyshev.parserrss.models.interactor

import org.jetbrains.anko.doAsync
import ru.iandreyshev.parserrss.factory.useCase.IUseCaseFactory
import ru.iandreyshev.parserrss.factory.useCase.UseCaseType
import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener

class ArticleInteractor(
        private val mUseCaseFactory: IUseCaseFactory,
        private val mListener: IUseCaseListener,
        private val mArticleId: Long) {

    private val mOpenOriginalUseCase = mUseCaseFactory
            .create(UseCaseType.ARTICLE_OPEN_ORIGINAL, mArticleId, mListener)

    init {
        doAsync {
            mUseCaseFactory
                    .create(UseCaseType.ARTICLE_LOAD_DATA, mArticleId, mListener)
                    .start()
            mUseCaseFactory
                    .create(UseCaseType.ARTICLE_LOAD_IMAGE, mArticleId, mListener)
                    .start()
        }
    }

    fun onOpenOriginal() = doAsync {
        mOpenOriginalUseCase.start()
    }
}
