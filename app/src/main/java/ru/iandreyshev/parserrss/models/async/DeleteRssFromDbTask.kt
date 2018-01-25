package ru.iandreyshev.parserrss.models.async

import android.util.Log

import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.rss.ViewRss

class DeleteRssFromDbTask private constructor(private val listener: IEventListener, private val rssToDelete: ViewRss)
    : Task<ViewRss, Any, ViewRss>(listener) {

    companion object {
        private val TAG = DeleteRssFromDbTask::class.java.name

        fun execute(listener: IEventListener, rssToDelete: ViewRss) {
            DeleteRssFromDbTask(listener, rssToDelete).executeOnExecutor(Task.EXECUTOR)
        }
    }

    interface IEventListener : ITaskListener<ViewRss, Any, ViewRss> {
        fun onFail(rss: ViewRss)
    }

    override fun doInBackground(vararg rssToDelete: ViewRss): ViewRss? {
        try {
            App.getDatabase().removeRssById(this.rssToDelete.id)
        } catch (ex: Exception) {
            Log.e(TAG, Log.getStackTraceString(ex))
            setResultEvent({ listener.onFail(this.rssToDelete) })
        }

        return this.rssToDelete
    }
}
