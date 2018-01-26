package ru.iandreyshev.parserrss.models.async

import android.util.Log

import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.ViewRss

class InsertNewRssTask private constructor(
        override val listener: IEventListener,
        url: String,
        private val filter: IArticlesFilter) : GetRssFromNetTask(listener, url) {

    companion object {
        private val TAG = InsertNewRssTask::class.java.name

        fun execute(listener: IEventListener, url: String, filter: IArticlesFilter) {
            InsertNewRssTask(listener, url, filter).executeOnExecutor(Task.EXECUTOR)
        }
    }

    private val mDatabase = App.database

    override fun isUrlValid(): Boolean {
        if (!super.isUrlValid()) {
            setResultEvent { listener.onInvalidUrl() }

            return false

        } else if (mDatabase.isRssWithUrlExist(url)) {
            setResultEvent { listener.onRssAlreadyExist() }

            return false
        }

        return true
    }

    override fun onSuccess(rss: Rss) {
        try {
            if (mDatabase.putRssIfSameUrlNotExist(rss)) {
                rss.articles = filter.sort(rss.articles)
                setResultEvent { listener.onSuccess(ViewRss(rss)) }
            } else {
                setResultEvent { listener.onRssAlreadyExist() }
            }
        } catch (ex: Exception) {
            Log.e(TAG, Log.getStackTraceString(ex))
            setResultEvent { listener.onDatabaseError() }
        }
    }

    interface IEventListener : GetRssFromNetTask.IEventListener {
        fun onRssAlreadyExist()

        fun onDatabaseError()

        fun onSuccess(rss: ViewRss)
    }
}
