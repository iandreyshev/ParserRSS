package ru.iandreyshev.parserrss.models.async

import android.util.Log

import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss

class UpdateRssFromNetTask private constructor(
        override val listener: IEventListener,
        url: String,
        private val filter: IArticlesFilter) : GetRssFromNetTask(listener, url) {

    companion object {
        private val TAG = UpdateRssFromNetTask::class.java.name

        fun execute(listener: IEventListener, url: String, filter: IArticlesFilter) {
            UpdateRssFromNetTask(listener, url, filter).executeOnExecutor(Task.EXECUTOR)
        }
    }

    override fun isUrlValid(): Boolean {
        if (!super.isUrlValid()) {
            setResultEvent { listener.onInvalidUrl() }

            return false

        } else if (!App.database.isRssWithUrlExist(url)) {
            setResultEvent { listener.onRssNotExist() }

            return false
        }

        return true
    }

    override fun onSuccess(rss: Rss) {
        try {
            if (App.database.updateRssWithSameUrl(rss)) {
                rss.articles = filter.sort(rss.articles)
                setResultEvent { listener.onSuccess(ViewRss(rss).articles) }
            } else {
                setResultEvent { listener.onRssNotExist() }
            }
        } catch (ex: Exception) {
            Log.e(TAG, Log.getStackTraceString(ex))
            setResultEvent { listener.onDatabaseError() }
        }
    }

    interface IEventListener : GetRssFromNetTask.IEventListener {
        fun onRssNotExist()

        fun onDatabaseError()

        fun onSuccess(articles: List<ViewArticle>)
    }
}
