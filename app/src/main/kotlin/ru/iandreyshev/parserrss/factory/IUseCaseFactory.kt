package ru.iandreyshev.parserrss.factory

import ru.iandreyshev.parserrss.models.useCase.UseCaseType
import ru.iandreyshev.parserrss.models.useCase.IUseCase

interface IUseCaseFactory {
    fun create(type: UseCaseType, presenter: Any, data: Any): IUseCase
}
