package ru.iandreyshev.parserrss.models.repository

import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import ru.iandreyshev.parserrss.models.rss.Article
import ru.iandreyshev.parserrss.models.rss.ArticleImage
import ru.iandreyshev.parserrss.models.rss.Rss

import java.io.File
import java.util.ArrayList

class ObjectBoxRepositoryTest {

    companion object {
        private const val RSS_TITLE = "TITLE"
        private const val RSS_ORIGIN = "ORIGIN"
        private const val RSS_URL = "URL"
        private const val NOT_USE_RSS_URL = "NOT_USE_URL"
        private const val NOT_USE_RSS_ID = Long.MAX_VALUE

        private const val ARTICLES_COUNT = 25
        private const val ARTICLE_TITLE = "TITLE %s"
        private const val ARTICLE_DESCRIPTION = "DESCRIPTION %s"
        private const val ARTICLE_ORIGIN = "ORIGIN %s"
    }

    private lateinit var mBoxStore: BoxStore
    private lateinit var mRepository: ObjectBoxRepository
    private lateinit var mRss: Rss
    private lateinit var mImageBytes: ByteArray

    @Before
    fun setup() {
        val tempFile = File.createTempFile("object-store-test", "")

        assertTrue(tempFile.delete())

        mBoxStore = MyObjectBox.builder().directory(tempFile).build()
        mRepository = ObjectBoxRepository(mBoxStore)
        val articleList = ArrayList<Article>()

        repeat(ARTICLES_COUNT) {
            articleList.add(createArticle(it))
        }

        mImageBytes = byteArrayOf()
        mRss = Rss(
                title = RSS_TITLE,
                originUrl = RSS_ORIGIN,
                url = RSS_URL,
                articles = articleList)
    }

    @Test
    fun returnNullIfGetRssByInvalidId() {
        assertNull(mRepository.getRssById(-2))
    }

    @Test
    fun returnTrueIfRssWithSameUrlExist() {
        mRepository.putNewRss(mRss)
        assertTrue(mRepository.isRssWithUrlExist(RSS_URL))
    }

    @Test
    fun returnFalseIfRssWithSameUrlNotExist() {
        mRepository.putNewRss(mRss)
        assertFalse(mRepository.isRssWithUrlExist(NOT_USE_RSS_URL))
    }

    @Test
    fun returnArticlesById() {
        mRepository.putNewRss(mRss)

        for ((id) in mRss.articles) {
            assertNotNull(mRepository.getArticleById(id))
        }
    }

    @Test
    fun updateRssIdAfterPut() {
        mRepository.putNewRss(mRss)

        assertEquals(mRss.id, mRepository.getRssById(mRss.id)?.id)
    }

    @Test
    fun returnTrueWhenUpdatingRssExist() {
        val newRss = Rss(title = "Title", originUrl = "Origin")
        newRss.description = "Description"
        newRss.url = mRss.url

        assertNotEquals(mRss.title, newRss.title)
        assertNotEquals(mRss.description, newRss.description)
        assertNotEquals(mRss.articles.size.toLong(), newRss.articles.size.toLong())

        assertEquals(mRss.url, newRss.url)

        assertEquals(IRepository.InsertRssResult.SUCCESS, mRepository.putNewRss(newRss))
        assertTrue(mRepository.updateRssWithSameUrl(mRss))

        assertNotEquals(newRss.title, mRss.title)
        assertNotEquals(newRss.description, mRss.description)
    }

    @Test
    fun deleteArticlesIfTheyNotInRssToUpdate() {
        val rss = Rss(url = RSS_URL)
        val articlesCount = 10
        repeat(articlesCount) {
            rss.articles.add(Article(originUrl = it.toString()))
        }

        assertEquals(IRepository.InsertRssResult.SUCCESS, mRepository.putNewRss(rss))

        val updatedRss = Rss(url = rss.url)
        val newCount = articlesCount / 2
        repeat(newCount) {
            rss.articles.add(Article(originUrl = it.toString()))
        }

        assertTrue(mRepository.updateRssWithSameUrl(updatedRss))
        val rssFromRepository = mRepository.getRssById(updatedRss.id)
        repeat(articlesCount) { index ->
            val articleWithCurrentUrl = rssFromRepository?.articles?.find { article ->
                article.originUrl == index.toString()
            }

            if (articleWithCurrentUrl != null && index > newCount) {
                fail()
            }
        }
    }

    @Test
    fun returnFalseIfTryToRemoveNotExistsRss() {
        assertFalse(mRepository.removeRssById(NOT_USE_RSS_ID))
    }

    @Test
    fun returnFalseWhenUpdatingNotExistRss() {
        val newRss = Rss(title = "", originUrl = "", url = NOT_USE_RSS_URL)

        assertEquals(IRepository.InsertRssResult.SUCCESS, mRepository.putNewRss(newRss))
        assertFalse(mRepository.updateRssWithSameUrl(mRss))
    }

    @Test
    fun removeRss() {
        assertEquals(IRepository.InsertRssResult.SUCCESS, mRepository.putNewRss(mRss))
        mRepository.removeRssById(mRss.id)
        assertNull(mRepository.getRssById(mRss.id))
    }

    @Test
    fun returnExistStateAfterPutRssWithSameUrl() {
        mRepository.putNewRss(mRss)
        val rssWithSameUrl = Rss(title = "", originUrl = "", url = RSS_URL)
        rssWithSameUrl.url = mRss.url
        assertEquals(IRepository.InsertRssResult.EXIST, mRepository.putNewRss(rssWithSameUrl))
    }

    @Test
    fun updateArticlesRssIdAfterPut() {
        mRepository.putNewRss(mRss)
        mRss.articles.forEach { assertTrue(it.rssId == mRss.id) }
    }

    @Test
    fun returnRssWithSameArticles() {
        mRepository.putNewRss(mRss)
        val rss = mRepository.getRssById(mRss.id)

        assertNotNull(rss)
        rss?.articles?.forEach { assertTrue(it in mRss.articles) }
    }

    @Test
    fun setZeroMaxValuesIfTryToSetLessThanZero() {
        val repository = ObjectBoxRepository(mBoxStore, -1, -1)

        assertEquals(0, repository.maxRssCount)
        assertEquals(0, repository.maxArticlesInRssCount)
    }

    @Test
    fun returnFullStateIfTryToAddRssToFullRepository() {
        val repository = ObjectBoxRepository(mBoxStore, 0, 0)

        assertEquals(IRepository.InsertRssResult.FULL, repository.putNewRss(mRss))
    }

    @Test
    fun takeOnlyMaxArticlesCountFromRss() {
        val maxArticlesCount = 10
        val repository = ObjectBoxRepository(mBoxStore, 1, maxArticlesCount)
        val rss = Rss()
        rss.articles.addAll(Array(maxArticlesCount * 2) {
            createArticle(it)
        })

        assertEquals(maxArticlesCount * 2, rss.articles.count())
        assertEquals(IRepository.InsertRssResult.SUCCESS, repository.putNewRss(rss))

        val rssFromRepository = repository.getRssById(rss.id)
        assertEquals(maxArticlesCount, rssFromRepository?.articles?.count())
    }

    @Test
    fun returnTrueIfRssCountIsMax() {
        val alwaysFullRepository = ObjectBoxRepository(mBoxStore, 0, 0)

        assertTrue(alwaysFullRepository.isFull)

        val count = 10
        val fullRssRepository = ObjectBoxRepository(mBoxStore, count, 0)
        repeat(count) {
            val rss = Rss(url = it.toString())

            assertEquals(IRepository.InsertRssResult.SUCCESS, fullRssRepository.putNewRss(rss))
        }

        assertTrue(fullRssRepository.isFull)
    }

    @Test
    fun returnEmptyRssIdListIfRepositoryIsEmpty() {
        assertTrue(mRepository.rssIdList.isEmpty())

        val rssIdList = ArrayList<Long>()
        repeat(mRepository.maxRssCount) {
            val rss = Rss(url = it.toString())

            assertEquals(IRepository.InsertRssResult.SUCCESS, mRepository.putNewRss(rss))

            rssIdList.add(rss.id)
        }

        mRepository.rssIdList.forEach {
            assertTrue(it in rssIdList)
        }
    }

    @Test
    fun deleteArticlesAndImagesIfRssWithArticleRemoved() {
        val imageUrl = "Image url"
        val rss = Rss()
        rss.articles.add(Article(imageUrl = imageUrl))

        assertEquals(IRepository.InsertRssResult.SUCCESS, mRepository.putNewRss(rss))
        assertNotNull(mRepository.getRssById(rss.id))

        val articleId = rss.articles[0].id
        val articleImage = ArticleImage(
                articleId = articleId,
                bytes = mImageBytes
        )
        val putImageResult = mRepository.putArticleImageIfArticleExist(articleImage)

        assertTrue(putImageResult)
        assertNotNull(mRepository.getArticleById(articleId))
        assertNotNull(mRepository.getArticleImageByArticleId(articleId))

        assertTrue(mRepository.removeRssById(rss.id))
        assertNull(mRepository.getRssById(rss.id))
        assertNull(mRepository.getArticleById(articleId))
        assertNull(mRepository.getArticleImageByArticleId(articleId))
    }

    @Test
    fun notPutArticleImageIfArticleNotExist() {
        val notExistArticleId = 10L
        val articleImage = ArticleImage(
                articleId = notExistArticleId,
                bytes = mImageBytes
        )
        val insertionResult = mRepository.putArticleImageIfArticleExist(articleImage)

        assertFalse(insertionResult)
        assertEquals(0, mBoxStore.boxFor<ArticleImageEntity>().count())
    }

    @Test
    fun canSaveByteArray() {
        assertEquals(IRepository.InsertRssResult.SUCCESS, mRepository.putNewRss(mRss))

        val articleId = mRss.articles[0].id
        val bytes = byteArrayOf(0, 1, 2, 3, 4, 5)
        val articleImage = ArticleImage(
                articleId = articleId,
                bytes = bytes
        )

        assertTrue(mRepository.putArticleImageIfArticleExist(articleImage))

        val imageFromRepo = mRepository.getArticleImageByArticleId(articleId)

        assertTrue(bytes.contentEquals(imageFromRepo?.bytes ?: byteArrayOf()))
    }

    @Test
    fun amountOfNewsAfterTheUpdateIsNoMoreThanTheMaximum() {
        assertEquals(IRepository.InsertRssResult.SUCCESS, mRepository.putNewRss(mRss))
        assertTrue(mRepository.maxArticlesInRssCount > 0)

        mRss.articles.clear()
        repeat(mRepository.maxArticlesInRssCount * 2) {
            mRss.articles.add(createArticle(it))
        }

        assertTrue(mRepository.maxArticlesInRssCount < mRss.articles.count())
        assertTrue(mRepository.updateRssWithSameUrl(mRss))
        assertEquals(mRepository.maxArticlesInRssCount, mRss.articles.count())

        val rssFromRepository = mRepository.getRssById(mRss.id)

        assertEquals(mRepository.maxArticlesInRssCount, rssFromRepository?.articles?.count())
    }

    private fun createArticle(number: Int): Article {
        return Article(
                title = ARTICLE_TITLE.format(number),
                description = ARTICLE_DESCRIPTION.format(number),
                originUrl = ARTICLE_ORIGIN.format(number))
    }
}