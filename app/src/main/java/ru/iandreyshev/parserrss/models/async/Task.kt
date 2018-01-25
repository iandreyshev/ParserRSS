package ru.iandreyshev.parserrss.models.async

import android.os.AsyncTask
import android.support.annotation.CallSuper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

abstract class Task<T, U, V>(private val listener: ITaskListener<T, U, V>) : AsyncTask<T, U, V>() {
    companion object {
        private const val THREADS_COUNT = 25
        val EXECUTOR: Executor = Executors.newFixedThreadPool(THREADS_COUNT)
    }

    private var resultEvent: (() -> Unit)? = null

    @CallSuper
    override fun onPreExecute() {
        listener.onPreExecute()
    }

    @CallSuper
    override fun onProgressUpdate(vararg values: U) {
        listener.onProgressUpdate(*values)
    }

    @CallSuper
    override fun onPostExecute(result: V) {
        listener.onPostExecute(result)
        resultEvent?.invoke()
    }

    fun setResultEvent(resultEvent: (() -> Unit)?) {
        this.resultEvent = resultEvent
    }
}
