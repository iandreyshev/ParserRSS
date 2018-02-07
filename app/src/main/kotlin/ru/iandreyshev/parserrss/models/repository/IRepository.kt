package ru.iandreyshev.parserrss.models.repository

import android.graphics.Bitmap

interface IRepository {
    val isFull: Boolean

    val rssIdList: LongArray

    fun getRssById(id: Long): Rss?

    fun getArticleById(id: Long): Article?

    fun getRssTitle(id: Long): String

    fun getArticleImage(articleId: Long): ArticleImage?

    fun getArticleImageBitmap(articleId: Long): Bitmap?

    fun getArticleImageUrl(articleId: Long): String?

    fun putNewRss(newRss: Rss): PutRssState

    fun putArticleImageIfArticleExist(articleId: Long, imageBitmap: Bitmap): Boolean

    fun updateRssWithSameUrl(newRss: Rss): Boolean

    fun isRssWithUrlExist(url: String): Boolean

    fun removeRssById(id: Long): Boolean

    enum class PutRssState {
        SUCCESS,
        EXIST,
        FULL
    }
}
