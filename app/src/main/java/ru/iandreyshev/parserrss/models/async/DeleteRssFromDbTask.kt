package ru.iandreyshev.parserrss.models.async

import android.util.Log

import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.rss.ViewRss

class DeleteRssFromDbTask private constructor(
        _listener: ITaskListener<ViewRss, Any, ViewRss>,
        private val _rssToDelete: ViewRss) : Task<ViewRss, Any, ViewRss>(_listener) {

    companion object {
        private val TAG = DeleteRssFromDbTask::class.java.name

        fun execute(listener: ITaskListener<ViewRss, Any, ViewRss>, rssToDelete: ViewRss) {
            DeleteRssFromDbTask(listener, rssToDelete).executeOnExecutor(Task.EXECUTOR)
        }
    }

    override fun doInBackground(vararg rssToDelete: ViewRss): ViewRss? {
        return try {
            App.database.removeRssById(this._rssToDelete.id)
            this._rssToDelete
        } catch (ex: Exception) {
            Log.e(TAG, Log.getStackTraceString(ex))
            null
        }
    }
}
