package ru.iandreyshev.parserrss.interactor

import ru.iandreyshev.parserrss.factory.IUseCaseFactory
import ru.iandreyshev.parserrss.models.useCase.IUseCase
import ru.iandreyshev.parserrss.models.useCase.UseCaseType
import ru.iandreyshev.parserrss.presentation.presenter.IPresenter

class ArticleInteractor(useCaseFactory: IUseCaseFactory, presenter: IPresenter, articleId: Long) {

    private val mOpenOriginalUseCase: IUseCase

    init {
        useCaseFactory.create(
                UseCaseType.LOAD_AND_OPEN_ARTICLE,
                presenter,
                articleId
        ).start()

        useCaseFactory.create(
                UseCaseType.GET_ARTICLE_IMAGE,
                presenter,
                articleId
        ).start()

        mOpenOriginalUseCase = useCaseFactory.create(
                UseCaseType.OPEN_ARTICLE_ORIGINAL,
                presenter,
                articleId
        )
    }

    fun onOpenOriginal() = mOpenOriginalUseCase.start()
}
