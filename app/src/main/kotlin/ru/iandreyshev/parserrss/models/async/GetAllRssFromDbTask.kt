package ru.iandreyshev.parserrss.models.async

import android.util.Log

import java.util.ArrayList

import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.rss.ViewRss

class GetAllRssFromDbTask private constructor(
        private val mListener: IEventListener,
        private val mFilter: IArticlesFilter) : Task<Any, ViewRss, Any?>(mListener) {

    companion object {
        private val TAG = GetAllRssFromDbTask::class.java.name

        fun execute(listener: IEventListener, filter: IArticlesFilter) {
            GetAllRssFromDbTask(listener, filter).executeOnExecutor(Task.EXECUTOR)
        }
    }

    override fun doInBackground(vararg args: Any): Any? {
        val result = ArrayList<ViewRss>()

        try {
            App.repository.getRssIdList().forEach { id ->
                val rss = App.repository.getRssById(id)

                if (rss != null) {
                    mFilter.sort(rss.articles)
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
        mListener.onLoad(values[0])
    }

    interface IEventListener : ITaskListener<Any, ViewRss, Any?> {
        fun onLoad(rss: ViewRss)
    }
}
