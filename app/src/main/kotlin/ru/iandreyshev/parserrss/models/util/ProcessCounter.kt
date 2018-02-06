package ru.iandreyshev.parserrss.models.util

class ProcessCounter(private val mListener: (Int) -> Unit) {
    private var mCount: Int = 0

    init {
        mListener(mCount)
    }

    fun add() {
        if (mCount < Int.MAX_VALUE) {
            ++mCount
            mListener(mCount)
        }
    }

    fun remove() {
        if (mCount > 0) {
            --mCount
            mListener(mCount)
        }
    }
}
