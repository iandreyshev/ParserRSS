package ru.iandreyshev.parserrss.models.repository

import android.graphics.Bitmap

interface IRepository {
    val isFull: Boolean

    val maxArticlesInRss: Int

    val rssIdList: LongArray

    fun getRssById(id: Long): Rss?

    fun getArticleById(id: Long): Article?

    fun getRssTitleByRssId(id: Long): String?

    fun getArticleImageByArticleId(id: Long): ArticleImage?

    fun getArticleImageBitmapByArticleId(id: Long): Bitmap?

    fun getArticleImageUrlByArticleId(id: Long): String?

    fun putNewRss(newRss: Rss): InsertRssResult

    fun putArticleImageIfArticleExist(articleId: Long, imageBitmap: Bitmap): Boolean

    fun updateRssWithSameUrl(newRss: Rss): Boolean

    fun isRssWithUrlExist(url: String): Boolean

    fun removeRssById(id: Long): Boolean

    enum class InsertRssResult {
        SUCCESS,
        EXIST,
        FULL
    }
}
