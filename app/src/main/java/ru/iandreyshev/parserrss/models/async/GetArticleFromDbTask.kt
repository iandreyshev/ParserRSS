package ru.iandreyshev.parserrss.models.async

import android.util.Log

import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss

class GetArticleFromDbTask private constructor(private val articleId: Long, private val listener: IEventListener)
    : Task<Long, Any, Any?>(listener) {

    companion object {
        private val TAG = GetArticleFromDbTask::class.java.name

        fun execute(id: Long, listener: IEventListener) {
            GetArticleFromDbTask(id, listener).executeOnExecutor(Task.EXECUTOR)
        }
    }

    private var viewRss: ViewRss? = null
    private var viewArticle: ViewArticle? = null

    interface IEventListener : ITaskListener<Long, Any, Any?> {
        fun onSuccess(rss: ViewRss, article: ViewArticle)

        fun onFail()
    }

    override fun doInBackground(vararg args: Long?): Any? {
        try {
            val article = App.getDatabase().getArticleById(articleId) ?: return null
            viewArticle = ViewArticle(article)
            viewRss = ViewRss(
                    id = article.rssId,
                    title = App.getDatabase().getRssTitle(article.rssId)
            )
        } catch (ex: Exception) {
            Log.e(TAG, Log.getStackTraceString(ex))
        }

        return null
    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)

        val rss = viewRss
        val article = viewArticle

        if (rss != null && article != null) {
            listener.onSuccess(rss, article)
        } else {
            listener.onFail()
        }
    }
}
