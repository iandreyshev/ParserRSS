package ru.iandreyshev.parserrss.models.repository

import android.graphics.Bitmap

interface IRepository {
    val isFull: Boolean

    val maxRssCount: Int

    val maxArticlesInRssCount: Int

    val rssIdList: LongArray

    fun runInTx(callback: () -> Unit)

    fun getRssById(id: Long): Rss?

    fun getArticleById(id: Long): Article?

    fun getArticleImageByArticleId(id: Long): ArticleImage?

    fun getArticlesByRssId(id: Long): MutableList<Article>

    fun isRssWithUrlExist(url: String): Boolean

    fun putArticleImageIfArticleExist(articleId: Long, imageBitmap: Bitmap): Boolean

    fun putNewRss(newRss: Rss): InsertRssResult

    fun updateRssWithSameUrl(newRss: Rss): Boolean

    fun removeRssById(id: Long): Boolean

    enum class InsertRssResult {
        SUCCESS,
        EXIST,
        FULL
    }
}
