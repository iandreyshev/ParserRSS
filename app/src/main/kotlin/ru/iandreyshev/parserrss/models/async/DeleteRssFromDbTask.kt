package ru.iandreyshev.parserrss.models.async

import android.util.Log

import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.rss.ViewRss

class DeleteRssFromDbTask private constructor(
        _listener: ITaskListener<ViewRss, Any, ViewRss>,
        private val mRssToDelete: ViewRss) : Task<ViewRss, Any, ViewRss>(_listener) {

    companion object {
        private val TAG = DeleteRssFromDbTask::class.java.name

        fun execute(listener: ITaskListener<ViewRss, Any, ViewRss>, rssToDelete: ViewRss) {
            DeleteRssFromDbTask(listener, rssToDelete).executeOnExecutor(Task.EXECUTOR)
        }
    }

    override fun doInBackground(vararg rssToDelete: ViewRss): ViewRss? {
        return try {
            App.repository.removeRssById(this.mRssToDelete.id)
            mRssToDelete
        } catch (ex: Exception) {
            Log.e(TAG, Log.getStackTraceString(ex))
            null
        }
    }
}
