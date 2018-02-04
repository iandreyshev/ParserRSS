package ru.iandreyshev.parserrss.models.useCase

import android.util.Log
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

import ru.iandreyshev.parserrss.models.repository.IRepository

class DeleteRssUseCase(
        private val mRepository: IRepository,
        private val mRssId: Long,
        private val mListener: IListener) : IUseCase {

    companion object {
        private val TAG = DeleteRssUseCase::class.java.name
    }

    interface IListener {
        fun onDeleteRssSuccess(rssId: Long)

        fun onDeleteRssFail()
    }

    override fun start() {
        doAsync {
            try {
                mRepository.removeRssById(mRssId)

                uiThread {
                    mListener.onDeleteRssSuccess(mRssId)
                }
            } catch (ex: Exception) {
                Log.e(TAG, Log.getStackTraceString(ex))
                uiThread {
                    mListener.onDeleteRssFail()
                }
            }
        }
    }
}
