package ru.iandreyshev.parserrss.factory.useCase

import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener
import ru.iandreyshev.parserrss.models.useCase.UseCase

interface IUseCaseFactory {
    fun create(type: UseCaseType, data: Any?, listener: IUseCaseListener): UseCase
}
