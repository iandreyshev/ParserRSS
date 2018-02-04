package ru.iandreyshev.parserrss.models.async

import android.os.AsyncTask
import android.support.annotation.CallSuper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

abstract class Task<T, U, V>(private val mListener: ITaskListener<T, U, V>) : AsyncTask<T, U, V>() {

    companion object {
        private const val THREADS_COUNT = 25
        val EXECUTOR: Executor = Executors.newFixedThreadPool(THREADS_COUNT)
    }

    private var mResultEvent: (() -> Unit)? = null

    @CallSuper
    override fun onPreExecute() {
        mListener.onPreExecute()
    }

    @CallSuper
    override fun onProgressUpdate(vararg values: U) {
        mListener.onProgressUpdate(*values)
    }

    @CallSuper
    override fun onPostExecute(result: V) {
        mListener.onPostExecute(result)
        mResultEvent?.invoke()
    }

    fun setResultEvent(resultEvent: (() -> Unit)?) {
        mResultEvent = resultEvent
    }
}
