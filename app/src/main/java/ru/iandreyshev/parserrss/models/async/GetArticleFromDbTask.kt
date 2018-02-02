package ru.iandreyshev.parserrss.models.async

import android.util.Log

import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss

class GetArticleFromDbTask private constructor(
        private val _articleId: Long,
        private val _listener: IEventListener) : Task<Long, Any, Any?>(_listener) {

    companion object {
        private val TAG = GetArticleFromDbTask::class.java.name

        fun execute(id: Long, listener: IEventListener) {
            GetArticleFromDbTask(id, listener).executeOnExecutor(Task.EXECUTOR)
        }
    }

    private var _rss: ViewRss? = null
    private var _article: ViewArticle? = null

    override fun doInBackground(vararg args: Long?): Any? {
        try {
            val article = App.database.getArticleById(_articleId) ?: return null
            _article = ViewArticle(article)
            _rss = ViewRss(
                    id = article.rssId,
                    title = App.database.getRssTitle(article.rssId)
            )
        } catch (ex: Exception) {
            Log.e(TAG, Log.getStackTraceString(ex))
        }

        return null
    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)

        val rss = _rss
        val article = _article

        if (rss != null && article != null) {
            _listener.onSuccess(rss, article)
        } else {
            _listener.onFail()
        }
    }

    interface IEventListener : ITaskListener<Long, Any, Any?> {
        fun onSuccess(rss: ViewRss, article: ViewArticle)

        fun onFail()
    }
}
