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

    fun getArticleImageByArticle(articleId: Long): ArticleImage?

    fun putArticleImage(articleId: Long, imageBitmap: Bitmap): Boolean
}
