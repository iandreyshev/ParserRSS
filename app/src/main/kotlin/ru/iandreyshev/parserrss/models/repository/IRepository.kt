package ru.iandreyshev.parserrss.models.repository

import ru.iandreyshev.parserrss.models.rss.Article
import ru.iandreyshev.parserrss.models.rss.ArticleImage
import ru.iandreyshev.parserrss.models.rss.Rss

interface IRepository {
    val isFull: Boolean

    val maxRssCount: Int

    val maxArticlesInRssCount: Int

    val rssIdList: List<Long>

    fun runInTx(callback: () -> Unit)

    fun getRssById(id: Long): Rss?

    fun getArticleById(id: Long): Article?

    fun getArticleImageByArticleId(id: Long): ArticleImage?

    fun isRssWithUrlExist(url: String): Boolean

    fun putArticleImageIfArticleExist(articleImage: ArticleImage): Boolean

    fun putNewRss(newRss: Rss): InsertRssResult

    fun updateRssWithSameUrl(newRss: Rss): Boolean

    fun removeRssById(id: Long): Boolean

    enum class InsertRssResult {
        SUCCESS,
        EXIST,
        FULL
    }
}
