package ru.iandreyshev.parserrss.interactor

open class BaseInteractor(private val _outputPort: IInteractorOutputPort) {

    private var _processCount: Int = 0

    init {
        _outputPort.onChangeProcessCount(_processCount)
    }

    interface IInteractorOutputPort {
        fun showMessage(messageId: Int) {}

        fun onChangeProcessCount(newCount: Int) {}
    }

    protected fun updateProcessCount(isAddProcess: Boolean = true) {
        _processCount += if (isAddProcess) 1 else -1

        if (_processCount < 0) {
            _processCount = 0
        }

        _outputPort.onChangeProcessCount(_processCount)
    }
}
