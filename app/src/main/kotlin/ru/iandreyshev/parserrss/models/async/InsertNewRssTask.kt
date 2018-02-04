package ru.iandreyshev.parserrss.models.async

import android.util.Log

import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.ViewRss

class InsertNewRssTask private constructor(
        private val mListener: IEventListener,
        private val mUrl: String,
        private val mFilter: IArticlesFilter) : GetRssFromNetTask(mListener, mUrl) {

    companion object {
        private val TAG = InsertNewRssTask::class.java.name

        fun execute(listener: IEventListener, url: String, filter: IArticlesFilter) {
            InsertNewRssTask(listener, url, filter).executeOnExecutor(Task.EXECUTOR)
        }
    }

    private val mDatabase = App.database

    override fun isUrlValid(): Boolean {
        if (!super.isUrlValid()) {
            setResultEvent { mListener.onInvalidUrl() }

            return false

        } else if (mDatabase.isRssWithUrlExist(mUrl)) {
            setResultEvent { mListener.onRssAlreadyExist() }

            return false
        }

        return true
    }

    override fun onSuccess(rss: Rss) {
        try {
            if (mDatabase.putRssIfSameUrlNotExist(rss)) {
                rss.articles = mFilter.sort(rss.articles)
                setResultEvent { mListener.onSuccess(ViewRss(rss)) }
            } else {
                setResultEvent { mListener.onRssAlreadyExist() }
            }
        } catch (ex: Exception) {
            Log.e(TAG, Log.getStackTraceString(ex))
            setResultEvent { mListener.onDatabaseError() }
        }
    }

    interface IEventListener : GetRssFromNetTask.IEventListener {
        fun onRssAlreadyExist()

        fun onDatabaseError()

        fun onSuccess(rss: ViewRss)
    }
}
