package ru.iandreyshev.parserrss.factory.useCase

import ru.iandreyshev.parserrss.models.useCase.IUseCase
import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener

interface IUseCaseFactory {
    fun create(type: UseCaseType, mListener: IUseCaseListener, data: Any = {}): IUseCase
}
