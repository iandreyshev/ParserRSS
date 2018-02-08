package ru.iandreyshev.parserrss.models.repository

import android.graphics.Bitmap
import java.util.HashSet

import io.objectbox.BoxStore

class RssRepository(
        private val mBoxStore: BoxStore,
        private val mMaxRssCount: Long = MAX_RSS_COUNT) : IRepository {

    companion object {
        const val MAX_RSS_COUNT = 5L
    }

    private val mRssBox = mBoxStore.boxFor(Rss::class.java)
    private val mArticleBox = mBoxStore.boxFor(Article::class.java)
    private val mArticleImageBox = mBoxStore.boxFor(ArticleImage::class.java)

    override val isFull: Boolean
        get() = mRssBox.count() == mMaxRssCount

    override val rssIdList: LongArray
        get() = mBoxStore.callInTx {
            mRssBox.query()
                    .notNull(Rss_.id)
                    .build()
                    .findIds()
        }

    override fun getRssById(id: Long): Rss? {
        val rss = getRss(id) ?: return null
        rss.articles = getArticlesByRssId(rss.id)

        return rss
    }

    override fun getArticleById(id: Long): Article? {
        return mArticleBox.get(id)
    }

    override fun isRssWithUrlExist(url: String): Boolean {
        return !mRssBox.find(Rss_.url, url).isEmpty()
    }

    override fun putNewRss(newRss: Rss): IRepository.PutRssState {
        return mBoxStore.callInTx {
            if (mRssBox.count() == mMaxRssCount) {
                IRepository.PutRssState.FULL
            }

            val rssWithSameUrl = mRssBox.query()
                    .equal(Rss_.url, newRss.url)
                    .build()
                    .findFirst()

            if (rssWithSameUrl == null) {
                mRssBox.put(newRss)
                putArticles(newRss)
                IRepository.PutRssState.SUCCESS
            } else {
                IRepository.PutRssState.EXIST
            }
        }
    }

    override fun updateRssWithSameUrl(newRss: Rss): Boolean {
        return mBoxStore.callInTx {
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

    override fun getRssTitleByRssId(id: Long): String? {
        return mRssBox.get(id)?.title
    }

    override fun removeRssById(id: Long): Boolean {
        return mBoxStore.callInTx {
            if (mRssBox.find(Rss_.id, id) == null) {
                return@callInTx false
            }

            mRssBox.remove(id)
            mArticleBox.query()
                    .equal(Article_.rssId, id)
                    .build()
                    .findIds()
                    .forEach { removeArticle(it) }

            true
        }
    }

    override fun getArticleImageByArticleId(id: Long): ArticleImage? {
        return mArticleImageBox.query()
                .equal(ArticleImage_.articleId, id)
                .build()
                .findFirst()
    }

    override fun putArticleImageIfArticleExist(articleId: Long, imageBitmap: Bitmap): Boolean {
        return mBoxStore.callInTx {
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

    override fun getArticleImageBitmapByArticleId(id: Long): Bitmap? {
        return getArticleImageByArticleId(id)?.bitmap
    }

    override fun getArticleImageUrlByArticleId(id: Long): String? {
        return getArticleById(id)?.imageUrl
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

    private fun bindArticles(rss: Rss) {
        rss.articles.forEach { it.rssId = rss.id }
    }

    private fun getArticlesByRssId(id: Long): MutableList<Article> {
        return mArticleBox.query()
                .equal(Article_.rssId, id)
                .build()
                .find()
    }

    private fun getRss(id: Long): Rss? {
        return mRssBox.get(id)
    }

    private fun removeArticle(id: Long) {
        mBoxStore.runInTx {
            mArticleBox.remove(id)
            mArticleImageBox.query()
                    .equal(ArticleImage_.articleId, id)
                    .build()
                    .remove()
        }
    }
}
