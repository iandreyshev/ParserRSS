package ru.iandreyshev.parserrss.models.repository

import java.util.HashSet

import io.objectbox.BoxStore
import ru.iandreyshev.parserrss.models.repository.extention.domainModel
import ru.iandreyshev.parserrss.models.repository.extention.entity
import ru.iandreyshev.parserrss.models.rss.Article
import ru.iandreyshev.parserrss.models.rss.ArticleImage
import ru.iandreyshev.parserrss.models.rss.Rss

class ObjectBoxRepository(
        private val mBoxStore: BoxStore,
        private var mMaxRssCount: Int = MAX_RSS_COUNT,
        private var mMaxArticlesInRssCount: Int = MAX_ARTICLES_IN_RSS_COUNT) : IRepository {

    companion object {
        private const val MAX_RSS_COUNT = 5
        private const val MAX_ARTICLES_IN_RSS_COUNT = 50
        private const val MIN_RSS_COUNT = 0
        private const val MIN_ARTICLES_COUNT = 0
    }

    private val mRssBox = mBoxStore.boxFor(RssEntity::class.java)
    private val mArticleBox = mBoxStore.boxFor(ArticleEntity::class.java)
    private val mArticleImageBox = mBoxStore.boxFor(ArticleImageEntity::class.java)

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

    override val rssIdList: List<Long>
        get() = mRssBox.all.map { it.id }

    override fun runInTx(callback: () -> Unit) {
        mBoxStore.runInTx(callback)
    }

    override fun getRssById(id: Long): Rss? {
        return mBoxStore.callInReadTx {
            val rss = mRssBox.get(id)

            if (rss != null) {
                rss.articles = getArticles(rss.id)
            }

            rss?.domainModel
        }
    }

    override fun getArticleById(id: Long): Article? {
        return mArticleBox.get(id)?.domainModel
    }

    override fun isRssWithUrlExist(url: String): Boolean {
        return !mRssBox.find(RssEntity_.url, url).isEmpty()
    }

    override fun putNewRss(newRss: Rss): IRepository.InsertRssResult {
        return mBoxStore.callInTx {
            val entity = newRss.entity

            if (mRssBox.count() == mMaxRssCount.toLong()) {
                return@callInTx IRepository.InsertRssResult.FULL
            }

            val rssWithSameUrl = mRssBox.query()
                    .equal(RssEntity_.url, entity.url)
                    .build()
                    .findFirst()

            return@callInTx if (rssWithSameUrl == null) {
                mRssBox.put(entity)
                putArticles(entity)
                newRss.id = entity.id
                newRss.articles = ArrayList(entity.articles.map { it.domainModel })
                IRepository.InsertRssResult.SUCCESS
            } else {
                IRepository.InsertRssResult.EXIST
            }
        }
    }

    override fun updateRssWithSameUrl(newRss: Rss): Boolean {
        return mBoxStore.callInTx {
            val entity = newRss.entity
            val rssWithSameUrl = mRssBox.find(RssEntity_.url, entity.url).firstOrNull()

            if (rssWithSameUrl == null) {
                false
            } else {
                entity.id = rssWithSameUrl.id
                newRss.id = entity.id
                mRssBox.put(entity)
                putArticles(entity)
                val newArticles = mArticleBox.find(ArticleEntity_.rssId, newRss.id)
                newRss.articles = ArrayList(newArticles.map { it.domainModel })
                true
            }
        }
    }

    override fun removeRssById(id: Long): Boolean {
        return mBoxStore.callInTx {
            if (mRssBox.get(id) == null) {
                false
            } else {
                mRssBox.remove(id)
                mArticleBox.find(ArticleEntity_.rssId, id).forEach { removeArticle(it.id) }
                true
            }
        }
    }

    override fun getArticleImageByArticleId(id: Long): ArticleImage? {
        return mArticleImageBox.find(ArticleImageEntity_.articleId, id).firstOrNull()?.domainModel
    }

    override fun putArticleImageIfArticleExist(articleImage: ArticleImage): Boolean {
        return mBoxStore.callInTx {
            val entity = articleImage.entity
            val article = mArticleBox.find(ArticleEntity_.id, entity.articleId).firstOrNull()

            if (article != null) {
                mArticleImageBox.put(entity)
                articleImage.id = entity.id
            }

            article != null
        }
    }

    private fun putArticles(rss: RssEntity) {
        val maxCount = Math.min(mMaxArticlesInRssCount, rss.articles.count())
        val newArticles = HashSet(rss.articles.take(maxCount))
        val currentArticles = getArticles(rss.id)

        currentArticles.forEach { currArticle ->
            if (!newArticles.remove(currArticle)) {
                mArticleBox.remove(currArticle.id)
                mArticleImageBox.remove(currArticle.id)
            }
        }

        newArticles.forEach { it.rssId = rss.id }
        mArticleBox.put(newArticles)
        rss.articles = getArticles(rss.id)
    }

    private fun getArticles(id: Long): MutableList<ArticleEntity> {
        return mArticleBox.find(ArticleEntity_.rssId, id)
    }

    private fun removeArticle(id: Long) {
        mBoxStore.runInTx {
            mArticleBox.remove(id)
            mArticleImageBox.query()
                    .equal(ArticleImageEntity_.articleId, id)
                    .build()
                    .remove()
        }
    }
}
