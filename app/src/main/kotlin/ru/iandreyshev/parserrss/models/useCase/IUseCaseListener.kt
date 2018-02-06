package ru.iandreyshev.parserrss.models.useCase

interface IUseCaseListener {
    fun processStart() {}

    fun processEnd() {}

    fun message(message: String) {}
}
