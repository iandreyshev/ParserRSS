package ru.iandreyshev.parserrss.models.useCase

import android.util.Log
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.models.web.IHttpRequestHandler

class InsertRssUseCase (
        private val mRepository: IRepository,
        private val mRequestHandler: IHttpRequestHandler,
        filter: IArticlesFilter,
        private val mListener: IListener)
    : DownloadRssUseCase(
        mRequestHandler,
        filter,
        mListener), IUseCase {

    companion object {
        private val TAG = InsertRssUseCase::class.java.name
    }

    private var mIsRssWithUrlExist = false

    interface IListener : DownloadRssUseCase.IListener {
        fun onRssAlreadyExist()

        fun onDatabaseError()

        fun onSuccess(rss: ViewRss)
    }

    override fun isUrlValidAsync(): Boolean {
        if (!super.isUrlValidAsync()) {

            return false

        } else if (mRepository.isRssWithUrlExist(mRequestHandler.urlString)) {
            mIsRssWithUrlExist = true

            return false
        }

        return true
    }

    override fun onUrlError() {
        if (mIsRssWithUrlExist) {
            mListener.onRssAlreadyExist()
        } else {
            mListener.onInvalidUrl()
        }
    }

    override fun onSuccessAsync(rss: Rss) {
        doAsync {
            try {
                if (mRepository.putRssIfSameUrlNotExist(rss)) {
                    uiThread {
                        mListener.onSuccess(ViewRss(rss))
                    }
                } else {
                    uiThread {
                        mListener.onRssAlreadyExist()
                    }
                }
            } catch (ex: Exception) {
                Log.e(TAG, Log.getStackTraceString(ex))
                uiThread {
                    mListener.onDatabaseError()
                }
            }
        }
    }
}
