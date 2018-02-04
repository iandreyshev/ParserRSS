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

    private val mRssBox = boxStore.boxFor(Rss::class.java)
    private val mArticleBox = boxStore.boxFor(Article::class.java)
    private val mArticleImageBox = boxStore.boxFor(ArticleImage::class.java)

    fun getRssIdList(): LongArray = boxStore.callInTx {
        mRssBox.query()
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

    fun isRssWithUrlExist(url: String): Boolean = !mRssBox.find(Rss_.url, url).isEmpty()

    fun putRssIfSameUrlNotExist(newRss: Rss): Boolean {
        return boxStore.callInTx {
            val rssWithSameUrl = mRssBox.query()
                    .equal(Rss_.url, newRss.url)
                    .build()
                    .findFirst()

            if (rssWithSameUrl == null) {
                mRssBox.put(newRss)
                putArticles(newRss)
                true
            } else {
                false
            }
        }
    }

    fun updateRssWithSameUrl(newRss: Rss): Boolean {
        return boxStore.callInTx {
            val rssWithSameUrl = mRssBox.query()
                    .equal(Rss_.url, newRss.url)
                    .build()
                    .findFirst()

            if (rssWithSameUrl == null) {
                false
            } else {
                newRss.id = rssWithSameUrl.id
                mRssBox.put(newRss)
                putArticles(newRss)
                true
            }
        }
    }

    fun getRssTitle(id: Long): String = mRssBox.get(id).title

    fun removeRssById(id: Long) {
        if (id in INVALID_IDS) {
            return
        }

        boxStore.runInTx {
            mRssBox.remove(id)
            mArticleBox.query()
                    .equal(Article_.rssId, id)
                    .build()
                    .findIds()
                    .forEach { removeArticle(it) }
        }
    }

    fun getArticleImageByArticle(articleId: Long): ArticleImage? {
        return mArticleImageBox.query()
                .equal(ArticleImage_.articleId, articleId)
                .build()
                .findFirst()
    }

    fun putArticleImage(articleId: Long, imageBitmap: Bitmap): Boolean {
        return boxStore.callInTx {
            val record = ArticleImage(articleId = articleId, bitmap = imageBitmap)
            val isArticleExist = mArticleBox.query()
                    .equal(Article_.id, articleId)
                    .build()
                    .findFirst()

            if (isArticleExist != null) {
                mArticleImageBox.put(record)
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
                mArticleBox.remove(it.id)
                mArticleImageBox.remove(it.id)
            }
        }

        mArticleBox.put(newArticles)
        rss.articles = getArticlesByRssId(rss.id)
    }

    private fun bindArticles(rss: Rss) = rss.articles.forEach { it.rssId = rss.id }

    private fun getArticlesByRssId(id: Long): MutableList<Article> {
        return if (id in INVALID_IDS) {
            ArrayList()
        } else {
            mArticleBox.query()
                    .equal(Article_.rssId, id)
                    .build()
                    .find()
        }
    }

    private fun getRss(id: Long): Rss? {
        return if (id in INVALID_IDS) null else mRssBox.get(id)
    }

    private fun getArticle(id: Long): Article? {
        return if (id in INVALID_IDS) null else mArticleBox.get(id)
    }

    private fun removeArticle(id: Long) {
        boxStore.runInTx {
            mArticleBox.remove(id)
            mArticleImageBox.query()
                    .equal(ArticleImage_.articleId, id)
                    .build()
                    .remove()
        }
    }
}
