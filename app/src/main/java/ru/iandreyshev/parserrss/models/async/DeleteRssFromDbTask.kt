package ru.iandreyshev.parserrss.models.async

import android.util.Log

import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.rss.ViewRss

class DeleteRssFromDbTask private constructor(
        listener: ITaskListener<ViewRss, Any, ViewRss>,
        private val rssToDelete: ViewRss) : Task<ViewRss, Any, ViewRss>(listener) {

    companion object {
        private val TAG = DeleteRssFromDbTask::class.java.name

        fun execute(listener: ITaskListener<ViewRss, Any, ViewRss>, rssToDelete: ViewRss) {
            DeleteRssFromDbTask(listener, rssToDelete).executeOnExecutor(Task.EXECUTOR)
        }
    }

    override fun doInBackground(vararg rssToDelete: ViewRss): ViewRss? {
        return try {
            App.database.removeRssById(this.rssToDelete.id)
            this.rssToDelete
        } catch (ex: Exception) {
            Log.e(TAG, Log.getStackTraceString(ex))
            null
        }
    }
}
