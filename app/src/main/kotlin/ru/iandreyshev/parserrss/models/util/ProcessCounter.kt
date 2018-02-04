package ru.iandreyshev.parserrss.models.util

class ProcessCounter(private val mListener: (Int) -> Unit) {
    private var mCount: Int = 0

    init {
        mListener(mCount)
    }

    fun startProcess() {
        if (mCount < Int.MAX_VALUE) {
            ++mCount
            mListener(mCount)
        }
    }

    fun endProcess() {
        if (mCount > 0) {
            --mCount
            mListener(mCount)
        }
    }
}
