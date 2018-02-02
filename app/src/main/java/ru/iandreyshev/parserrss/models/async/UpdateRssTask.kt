package ru.iandreyshev.parserrss.models.async

import android.util.Log

import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss

class UpdateRssTask private constructor(
        private val _listener: IEventListener,
        private val _url: String,
        private val _filter: IArticlesFilter) : GetRssFromNetTask(_listener, _url) {

    companion object {
        private val TAG = UpdateRssTask::class.java.name

        fun execute(listener: IEventListener, url: String, filter: IArticlesFilter) {
            UpdateRssTask(listener, url, filter).executeOnExecutor(Task.EXECUTOR)
        }
    }

    override fun isUrlValid(): Boolean {
        if (!super.isUrlValid()) {

            return false

        } else if (!App.database.isRssWithUrlExist(_url)) {
            setResultEvent { _listener.onRssNotExist() }

            return false
        }

        return true
    }

    override fun onSuccess(rss: Rss) {
        try {
            if (App.database.updateRssWithSameUrl(rss)) {
                rss.articles = _filter.sort(rss.articles)
                setResultEvent { _listener.onSuccess(ViewRss(rss).articles) }
            } else {
                setResultEvent { _listener.onRssNotExist() }
            }
        } catch (ex: Exception) {
            Log.e(TAG, Log.getStackTraceString(ex))
            setResultEvent { _listener.onDatabaseError() }
        }
    }

    interface IEventListener : GetRssFromNetTask.IEventListener {
        fun onRssNotExist()

        fun onDatabaseError()

        fun onSuccess(articles: List<ViewArticle>)
    }
}
