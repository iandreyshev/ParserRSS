package ru.iandreyshev.parserrss.models.async

import android.util.Log

import java.util.ArrayList

import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.rss.ViewRss

class GetAllRssFromDbTask private constructor(private val listener: IEventListener, private val filter: IArticlesFilter)
    : Task<Any, ViewRss, Any?>(listener) {

    companion object {
        private val TAG = GetAllRssFromDbTask::class.java.name

        fun execute(listener: IEventListener, filter: IArticlesFilter) {
            GetAllRssFromDbTask(listener, filter).executeOnExecutor(Task.EXECUTOR)
        }
    }

    interface IEventListener : ITaskListener<Any, ViewRss, Any?> {
        fun onLoad(rss: ViewRss)
    }

    override fun doInBackground(vararg args: Any): Any? {
        val result = ArrayList<ViewRss>()

        try {
            val idList = App.getDatabase().rssIdList

            idList.forEach { id ->
                val rss = App.getDatabase().getRssById(id)

                if (rss != null) {
                    rss.articles = filter.sort(rss.articles)
                    publishProgress(ViewRss(rss))
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, Log.getStackTraceString(ex))
        }

        return result
    }

    override fun onProgressUpdate(vararg values: ViewRss) {
        super.onProgressUpdate(*values)
        listener.onLoad(values[0])
    }
}
