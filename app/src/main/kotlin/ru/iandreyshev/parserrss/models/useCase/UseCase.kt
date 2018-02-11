package ru.iandreyshev.parserrss.models.useCase

abstract class UseCase(private val mListener: IUseCaseListener) {

    fun start() {
        if (isProcessNotifyAvailable()) {
            mListener.processStart()
        }

        if (isStartProcessAvailable()) {
            onProcess()
        }

        if (isProcessNotifyAvailable()) {
            mListener.processEnd()
        }
    }

    protected open fun isStartProcessAvailable(): Boolean {
        return true
    }

    protected abstract fun onProcess()

    protected open fun isProcessNotifyAvailable(): Boolean {
        return true
    }
}
