package ru.iandreyshev.parserrss.models.useCase

import android.os.AsyncTask
import java.util.concurrent.Executor
import java.util.concurrent.Executors

abstract class BaseUseCase<T, U, V>(
        private val mListener: IUseCaseListener): AsyncTask<T, U, V>(), IUseCase {

    companion object {
        private const val THREADS_COUNT = 25
        private val EXECUTOR: Executor = Executors.newFixedThreadPool(THREADS_COUNT)
    }

    override fun start() {
        executeOnExecutor(EXECUTOR)
    }

    override fun onPreExecute() {
        mListener.processStart()
    }

    override fun onPostExecute(result: V) {
        mListener.processEnd()
    }
}
