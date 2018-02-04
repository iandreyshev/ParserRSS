package ru.iandreyshev.parserrss.models.repository

import android.graphics.Bitmap

interface IRepository {
    fun getRssIdList(): LongArray

    fun getRssById(id: Long): Rss?

    fun getArticleById(id: Long): Article?

    fun isRssWithUrlExist(url: String): Boolean

    fun putRssIfSameUrlNotExist(newRss: Rss): Boolean

    fun updateRssWithSameUrl(newRss: Rss): Boolean

    fun getRssTitle(id: Long): String

    fun removeRssById(id: Long): Boolean

    fun getArticleImage(articleId: Long): ArticleImage?

    fun getArticleImageBitmap(articleId: Long): Bitmap?

    fun getArticleImageUrl(articleId: Long): String?

    fun putArticleImage(articleId: Long, imageBitmap: Bitmap): Boolean
}
