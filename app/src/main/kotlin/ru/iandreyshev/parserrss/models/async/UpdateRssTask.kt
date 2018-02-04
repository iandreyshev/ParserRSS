package ru.iandreyshev.parserrss.models.async

import android.util.Log

import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss

class UpdateRssTask private constructor(
        private val mListener: IEventListener,
        private val mUrl: String,
        private val mFilter: IArticlesFilter) : GetRssFromNetTask(mListener, mUrl) {

    companion object {
        private val TAG = UpdateRssTask::class.java.name

        fun execute(listener: IEventListener, url: String, filter: IArticlesFilter) {
            UpdateRssTask(listener, url, filter).executeOnExecutor(Task.EXECUTOR)
        }
    }

    override fun isUrlValid(): Boolean {
        if (!super.isUrlValid()) {

            return false

        } else if (!App.repository.isRssWithUrlExist(mUrl)) {
            setResultEvent { mListener.onRssNotExist() }

            return false
        }

        return true
    }

    override fun onSuccess(rss: Rss) {
        try {
            if (App.repository.updateRssWithSameUrl(rss)) {
                mFilter.sort(rss.articles)
                setResultEvent { mListener.onSuccess(ViewRss(rss).articles) }
            } else {
                setResultEvent { mListener.onRssNotExist() }
            }
        } catch (ex: Exception) {
            Log.e(TAG, Log.getStackTraceString(ex))
            setResultEvent { mListener.onDatabaseError() }
        }
    }

    interface IEventListener : GetRssFromNetTask.IEventListener {
        fun onRssNotExist()

        fun onDatabaseError()

        fun onSuccess(articles: List<ViewArticle>)
    }
}
