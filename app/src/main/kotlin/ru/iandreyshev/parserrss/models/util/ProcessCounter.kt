package ru.iandreyshev.parserrss.models.util

class ProcessCounter(private val mListener: (Int) -> Unit) {
    private var mProcessCount: Int = 0

    fun startProcess() {
        if (mProcessCount < Int.MAX_VALUE) {
            ++mProcessCount
            mListener(mProcessCount)
        }
    }

    fun endProcess() {
        if (mProcessCount > 0) {
            --mProcessCount
            mListener(mProcessCount)
        }
    }
}
