package ru.iandreyshev.parserrss.models.async

import android.util.Log

import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.ViewRss

class InsertNewRssTask private constructor(
        private val _listener: IEventListener,
        private val _url: String,
        private val _filter: IArticlesFilter) : GetRssFromNetTask(_listener, _url) {

    companion object {
        private val TAG = InsertNewRssTask::class.java.name

        fun execute(listener: IEventListener, url: String, filter: IArticlesFilter) {
            InsertNewRssTask(listener, url, filter).executeOnExecutor(Task.EXECUTOR)
        }
    }

    private val _database = App.database

    override fun isUrlValid(): Boolean {
        if (!super.isUrlValid()) {
            setResultEvent { _listener.onInvalidUrl() }

            return false

        } else if (_database.isRssWithUrlExist(_url)) {
            setResultEvent { _listener.onRssAlreadyExist() }

            return false
        }

        return true
    }

    override fun onSuccess(rss: Rss) {
        try {
            if (_database.putRssIfSameUrlNotExist(rss)) {
                rss.articles = _filter.sort(rss.articles)
                setResultEvent { _listener.onSuccess(ViewRss(rss)) }
            } else {
                setResultEvent { _listener.onRssAlreadyExist() }
            }
        } catch (ex: Exception) {
            Log.e(TAG, Log.getStackTraceString(ex))
            setResultEvent { _listener.onDatabaseError() }
        }
    }

    interface IEventListener : GetRssFromNetTask.IEventListener {
        fun onRssAlreadyExist()

        fun onDatabaseError()

        fun onSuccess(rss: ViewRss)
    }
}
