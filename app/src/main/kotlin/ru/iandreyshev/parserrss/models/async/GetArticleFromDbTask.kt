package ru.iandreyshev.parserrss.models.async

import android.util.Log

import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss

class GetArticleFromDbTask private constructor(
        private val mArticleId: Long,
        private val mListener: IEventListener) : Task<Long, Any, Any?>(mListener) {

    companion object {
        private val TAG = GetArticleFromDbTask::class.java.name

        fun execute(id: Long, listener: IEventListener) {
            GetArticleFromDbTask(id, listener).executeOnExecutor(Task.EXECUTOR)
        }
    }

    private var mRss: ViewRss? = null
    private var mArticle: ViewArticle? = null

    override fun doInBackground(vararg args: Long?): Any? {
        try {
            val article = App.repository.getArticleById(mArticleId) ?: return null
            mArticle = ViewArticle(article)
            mRss = ViewRss(
                    id = article.rssId,
                    title = App.repository.getRssTitle(article.rssId)
            )
        } catch (ex: Exception) {
            Log.e(TAG, Log.getStackTraceString(ex))
        }

        return null
    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)

        val rss = mRss
        val article = mArticle

        if (rss != null && article != null) {
            mListener.onSuccess(rss, article)
        } else {
            mListener.onFail()
        }
    }

    interface IEventListener : ITaskListener<Long, Any, Any?> {
        fun onSuccess(rss: ViewRss, article: ViewArticle)

        fun onFail()
    }
}
