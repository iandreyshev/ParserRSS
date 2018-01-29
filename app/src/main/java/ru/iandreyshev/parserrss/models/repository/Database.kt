package ru.iandreyshev.parserrss.models.repository

import java.util.ArrayList
import java.util.HashSet

import io.objectbox.BoxStore

class Database(private val boxStore: BoxStore) {
    companion object {
        internal val INVALID_IDS: MutableList<Long> = ArrayList()

        init {
            INVALID_IDS.add(-1L)
            INVALID_IDS.add(0L)
        }
    }

    private val rssBox = boxStore.boxFor(Rss::class.java)
    private val articleBox = boxStore.boxFor(Article::class.java)
    private val articleImageBox = boxStore.boxFor(ArticleImage::class.java)

    val rssIdList: LongArray
        @Throws(Exception::class)
        get() = boxStore.callInTx {
            rssBox.query()
                    .notNull(Rss_.id)
                    .build()
                    .findIds()
        }

    fun getRssById(id: Long): Rss? {
        val rss = getRss(id) ?: return null

        rss.articles = getArticlesByRssId(rss.id)

        return rss
    }

    fun getArticleById(id: Long): Article? {
        return getArticle(id)
    }

    fun isRssWithUrlExist(url: String): Boolean {
        return !rssBox.find(Rss_.url, url).isEmpty()
    }

    @Throws(Exception::class)
    fun putRssIfSameUrlNotExist(newRss: Rss): Boolean {
        return boxStore.callInTx {
            val rssWithSameUrl = rssBox.query()
                    .equal(Rss_.url, newRss.url)
                    .build()
                    .findFirst()

            if (rssWithSameUrl == null) {
                rssBox.put(newRss)
                putArticles(newRss)
                true
            } else {
                false
            }
        }
    }

    @Throws(Exception::class)
    fun updateRssWithSameUrl(newRss: Rss): Boolean {
        return boxStore.callInTx {
            val rssWithSameUrl = rssBox.query()
                    .equal(Rss_.url, newRss.url)
                    .build()
                    .findFirst()

            if (rssWithSameUrl == null) {
                false
            } else {
                newRss.id = rssWithSameUrl.id
                rssBox.put(newRss)
                putArticles(newRss)
                true
            }
        }
    }

    fun getRssTitle(id: Long): String {
        return rssBox.get(id).title
    }

    fun removeRssById(id: Long) {
        if (INVALID_IDS.contains(id)) {
            return
        }

        boxStore.runInTx {
            rssBox.remove(id)
            articleBox.query()
                    .equal(Article_.rssId, id)
                    .build()
                    .findIds()
                    .forEach { removeArticle(it) }
        }
    }

    fun getArticleImageByArticle(articleId: Long): ArticleImage? {
        return articleImageBox.query()
                .equal(ArticleImage_.articleId, articleId)
                .build()
                .findFirst()
    }

    fun putArticleImage(image: ArticleImage) {
        articleImageBox.put(image)
    }

    private fun putArticles(rss: Rss) {
        bindArticles(rss)

        val newArticles = HashSet(rss.articles)
        val currentArticles = getArticlesByRssId(rss.id)

        currentArticles.forEach {
            if (!newArticles.remove(it)) {
                articleBox.remove(it.id)
                articleImageBox.remove(it.id)
            }
        }

        articleBox.put(newArticles)
        rss.articles = getArticlesByRssId(rss.id)
    }

    private fun bindArticles(rss: Rss) {
        rss.articles.forEach { it.rssId = rss.id }
    }

    private fun getArticlesByRssId(id: Long): MutableList<Article> {
        return if (INVALID_IDS.contains(id)) {
            ArrayList()
        } else {
            articleBox.query()
                    .equal(Article_.rssId, id)
                    .build()
                    .find()
        }
    }

    private fun getRss(id: Long): Rss? {
        return if (INVALID_IDS.contains(id)) null else rssBox.get(id)
    }

    private fun getArticle(id: Long): Article? {
        return if (INVALID_IDS.contains(id)) null else articleBox.get(id)
    }

    private fun removeArticle(id: Long) {
        boxStore.runInTx {
            articleBox.remove(id)
            articleImageBox.query()
                    .equal(ArticleImage_.articleId, id)
                    .build()
                    .remove()
        }
    }
}
