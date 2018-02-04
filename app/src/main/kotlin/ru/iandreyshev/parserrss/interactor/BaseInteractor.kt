package ru.iandreyshev.parserrss.interactor

open class BaseInteractor(private val mOutputPort: IInteractorOutputPort) {

    private var mProcessCount: Int = 0

    init {
        mOutputPort.onChangeProcessCount(mProcessCount)
    }

    interface IInteractorOutputPort {
        fun showMessage(messageId: Int) {}

        fun onChangeProcessCount(newCount: Int) {}
    }

    protected fun updateProcessCount(isAddProcess: Boolean = true) {
        mProcessCount += if (isAddProcess) 1 else -1

        if (mProcessCount < 0) {
            mProcessCount = 0
        }

        mOutputPort.onChangeProcessCount(mProcessCount)
    }
}
