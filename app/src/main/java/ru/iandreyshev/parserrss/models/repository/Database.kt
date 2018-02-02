package ru.iandreyshev.parserrss.models.repository

import android.graphics.Bitmap
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

    private val _rssBox = boxStore.boxFor(Rss::class.java)
    private val _articleBox = boxStore.boxFor(Article::class.java)
    private val _articleImageBox = boxStore.boxFor(ArticleImage::class.java)

    fun getRssIdList(): LongArray = boxStore.callInTx {
        _rssBox.query()
                .notNull(Rss_.id)
                .build()
                .findIds()
    }

    fun getRssById(id: Long): Rss? {
        val rss = getRss(id) ?: return null
        rss.articles = getArticlesByRssId(rss.id)

        return rss
    }

    fun getArticleById(id: Long): Article? = getArticle(id)

    fun isRssWithUrlExist(url: String): Boolean = !_rssBox.find(Rss_.url, url).isEmpty()

    fun putRssIfSameUrlNotExist(newRss: Rss): Boolean {
        return boxStore.callInTx {
            val rssWithSameUrl = _rssBox.query()
                    .equal(Rss_.url, newRss.url)
                    .build()
                    .findFirst()

            if (rssWithSameUrl == null) {
                _rssBox.put(newRss)
                putArticles(newRss)
                true
            } else {
                false
            }
        }
    }

    fun updateRssWithSameUrl(newRss: Rss): Boolean {
        return boxStore.callInTx {
            val rssWithSameUrl = _rssBox.query()
                    .equal(Rss_.url, newRss.url)
                    .build()
                    .findFirst()

            if (rssWithSameUrl == null) {
                false
            } else {
                newRss.id = rssWithSameUrl.id
                _rssBox.put(newRss)
                putArticles(newRss)
                true
            }
        }
    }

    fun getRssTitle(id: Long): String = _rssBox.get(id).title

    fun removeRssById(id: Long) {
        if (id in INVALID_IDS) {
            return
        }

        boxStore.runInTx {
            _rssBox.remove(id)
            _articleBox.query()
                    .equal(Article_.rssId, id)
                    .build()
                    .findIds()
                    .forEach { removeArticle(it) }
        }
    }

    fun getArticleImageByArticle(articleId: Long): ArticleImage? {
        return _articleImageBox.query()
                .equal(ArticleImage_.articleId, articleId)
                .build()
                .findFirst()
    }

    fun putArticleImage(articleId: Long, imageBitmap: Bitmap): Boolean {
        return boxStore.callInTx {
            val record = ArticleImage(articleId = articleId, bitmap = imageBitmap)
            val isArticleExist = _articleBox.query()
                    .equal(Article_.id, articleId)
                    .build()
                    .findFirst()

            if (isArticleExist != null) {
                _articleImageBox.put(record)
            }

            isArticleExist != null
        }
    }

    private fun putArticles(rss: Rss) {
        bindArticles(rss)

        val newArticles = HashSet(rss.articles)
        val currentArticles = getArticlesByRssId(rss.id)

        currentArticles.forEach {
            if (!newArticles.remove(it)) {
                _articleBox.remove(it.id)
                _articleImageBox.remove(it.id)
            }
        }

        _articleBox.put(newArticles)
        rss.articles = getArticlesByRssId(rss.id)
    }

    private fun bindArticles(rss: Rss) = rss.articles.forEach { it.rssId = rss.id }

    private fun getArticlesByRssId(id: Long): MutableList<Article> {
        return if (id in INVALID_IDS) {
            ArrayList()
        } else {
            _articleBox.query()
                    .equal(Article_.rssId, id)
                    .build()
                    .find()
        }
    }

    private fun getRss(id: Long): Rss? {
        return if (id in INVALID_IDS) null else _rssBox.get(id)
    }

    private fun getArticle(id: Long): Article? {
        return if (id in INVALID_IDS) null else _articleBox.get(id)
    }

    private fun removeArticle(id: Long) {
        boxStore.runInTx {
            _articleBox.remove(id)
            _articleImageBox.query()
                    .equal(ArticleImage_.articleId, id)
                    .build()
                    .remove()
        }
    }
}
