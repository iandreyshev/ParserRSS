package ru.iandreyshev.parserrss.models.useCase

class MessageUseCase(
        private val mMessage: String,
        private val mListener: IUseCaseListener) : IUseCase {

    override fun start() = mListener.message(mMessage)
}
