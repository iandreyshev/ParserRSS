package ru.iandreyshev.parserrss.models.async

import android.os.AsyncTask
import android.support.annotation.CallSuper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

abstract class Task<T, U, V>(private val _listener: ITaskListener<T, U, V>) : AsyncTask<T, U, V>() {
    companion object {
        private const val THREADS_COUNT = 25
        val EXECUTOR: Executor = Executors.newFixedThreadPool(THREADS_COUNT)
    }

    private var _resultEvent: (() -> Unit)? = null

    @CallSuper
    override fun onPreExecute() {
        _listener.onPreExecute()
    }

    @CallSuper
    override fun onProgressUpdate(vararg values: U) {
        _listener.onProgressUpdate(*values)
    }

    @CallSuper
    override fun onPostExecute(result: V) {
        _listener.onPostExecute(result)
        _resultEvent?.invoke()
    }

    fun setResultEvent(resultEvent: (() -> Unit)?) {
        _resultEvent = resultEvent
    }
}
