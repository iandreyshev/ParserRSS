package ru.iandreyshev.parserrss.interactor

open class BaseInteractor(private val output: IInteractorOutputPort) {

    private var processCount: Int = 0

    init {
        output.onChangeProcessCount(processCount)
    }

    interface IInteractorOutputPort {
        fun showMessage(messageId: Int) {}

        fun onChangeProcessCount(newCount: Int) {}
    }

    protected fun updateProcessCount(isAddProcess: Boolean = true) {
        processCount += if (isAddProcess) 1 else -1

        if (processCount < 0) {
            processCount = 0
        }

        output.onChangeProcessCount(processCount)
    }
}
