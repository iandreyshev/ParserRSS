package ru.iandreyshev.parserrss.factory

import ru.iandreyshev.parserrss.models.useCase.UseCaseType
import ru.iandreyshev.parserrss.models.useCase.IUseCase
import ru.iandreyshev.parserrss.presentation.presenter.IPresenter

interface IUseCaseFactory {
    fun create(type: UseCaseType, presenter: IPresenter, data: Any): IUseCase
}
