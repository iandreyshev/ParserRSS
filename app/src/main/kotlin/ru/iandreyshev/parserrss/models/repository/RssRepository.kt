package ru.iandreyshev.parserrss.models.repository

import android.graphics.Bitmap
import java.util.HashSet

import io.objectbox.BoxStore

class RssRepository(
        private val mBoxStore: BoxStore,
        private var mMaxRssCount: Int = MAX_RSS_COUNT,
        private var mMaxArticlesInRssCount: Int = MAX_ARTICLES_IN_RSS_COUNT) : IRepository {

    companion object {
        private const val MAX_RSS_COUNT = 5
        private const val MAX_ARTICLES_IN_RSS_COUNT = 50
        private const val MIN_RSS_COUNT = 0
        private const val MIN_ARTICLES_COUNT = 0
    }

    private val mRssBox = mBoxStore.boxFor(Rss::class.java)
    private val mArticleBox = mBoxStore.boxFor(Article::class.java)
    private val mArticleImageBox = mBoxStore.boxFor(ArticleImage::class.java)

    init {
        mMaxRssCount = Math.max(mMaxRssCount, MIN_RSS_COUNT)
        mMaxArticlesInRssCount = Math.max(mMaxArticlesInRssCount, MIN_ARTICLES_COUNT)
    }

    override val maxRssCount: Int
        get() = mMaxRssCount

    override val maxArticlesInRssCount: Int
        get() = mMaxArticlesInRssCount

    override val isFull: Boolean
        get() = mRssBox.count() == mMaxRssCount.toLong()

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

    override fun putNewRss(newRss: Rss): IRepository.InsertRssResult {
        return mBoxStore.callInTx {
            if (mRssBox.count() == mMaxRssCount.toLong()) {
                return@callInTx IRepository.InsertRssResult.FULL
            }

            val rssWithSameUrl = mRssBox.query()
                    .equal(Rss_.url, newRss.url)
                    .build()
                    .findFirst()

            return@callInTx if (rssWithSameUrl == null) {
                mRssBox.put(newRss)
                putArticles(newRss)
                IRepository.InsertRssResult.SUCCESS
            } else {
                IRepository.InsertRssResult.EXIST
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
            if (mRssBox.get(id) == null) {
                false
            } else {
                mRssBox.remove(id)
                mArticleBox.query()
                        .equal(Article_.rssId, id)
                        .build()
                        .findIds()
                        .forEach { removeArticle(it) }

                true
            }
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
            val article = mArticleBox.query()
                    .equal(Article_.id, articleId)
                    .build()
                    .findFirst()

            if (article != null) {
                mArticleImageBox.put(record)
            }

            article != null
        }
    }

    override fun getArticleImageBitmapByArticleId(id: Long): Bitmap? {
        return getArticleImageByArticleId(id)?.bitmap
    }

    override fun getArticleImageUrlByArticleId(id: Long): String? {
        return getArticleById(id)?.imageUrl
    }

    private fun putArticles(rss: Rss) {
        val maxCount = Math.min(mMaxArticlesInRssCount, rss.articles.count())
        rss.articles = ArrayList(rss.articles.take(maxCount))

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
