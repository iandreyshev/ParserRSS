package ru.iandreyshev.parserrss.models.repository

import android.graphics.Bitmap
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

import java.io.File
import java.util.ArrayList
import java.util.HashSet

@RunWith(RobolectricTestRunner::class)
class RepositoryTest {

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
    private lateinit var mRepository: RssRepository
    private lateinit var mRss: Rss

    @Before
    fun setup() {
        val tempFile = File.createTempFile("object-store-test", "")

        assertTrue(tempFile.delete())

        mBoxStore = MyObjectBox.builder().directory(tempFile).build()
        mRepository = RssRepository(mBoxStore)
        val articleList = ArrayList<Article>()

        repeat(ARTICLES_COUNT) {
            articleList.add(createArticle(it))
        }

        mRss = Rss(
                title = RSS_TITLE,
                origin = RSS_ORIGIN,
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

        assertEquals(mRss, mRepository.getRssById(mRss.id))
    }

    @Test
    fun returnTrueWhenUpdatingRssExist() {
        val newRss = Rss(title = "Title", origin = "Origin")
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

        val currentArticles = mRepository.getRssById(updatedRss.id)?.articles
        repeat(articlesCount) { index ->
            val articleWithCurrentUrl = currentArticles?.find { article ->
                article.originUrl == index.toString()
            }

            if (articleWithCurrentUrl != null && index > newCount) {
                fail()
            }
        }
    }

    @Test
    fun returnRssTitleIfRssExist() {
        val rss = Rss(title = RSS_TITLE)

        assertEquals(IRepository.InsertRssResult.SUCCESS, mRepository.putNewRss(rss))
        assertEquals(mRepository.getRssTitleByRssId(rss.id), rss.title)
        assertNull(mRepository.getRssTitleByRssId(NOT_USE_RSS_ID))
    }

    @Test
    fun returnFalseIfTryToRemoveNotExistsRss() {
        assertFalse(mRepository.removeRssById(NOT_USE_RSS_ID))
    }

    @Test
    fun returnFalseWhenUpdatingNotExistRss() {
        val newRss = Rss(title = "", origin = "", url = NOT_USE_RSS_URL)

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
        val rssWithSameUrl = Rss(title = "", origin = "", url = RSS_URL)
        rssWithSameUrl.url = mRss.url
        assertEquals(IRepository.InsertRssResult.EXIST, mRepository.putNewRss(rssWithSameUrl))
    }

    @Test
    fun updateRssArticlesIdAfterPut() {
        mRepository.putNewRss(mRss)

        for ((_, rssId) in mRss.articles) {
            assertTrue(rssId == mRss.id)
        }
    }

    @Test
    fun returnRssWithSameArticles() {
        val articles = HashSet(mRss.articles)

        mRepository.putNewRss(mRss)
        val rssFromDatabase = mRepository.getRssById(mRss.id)

        if (rssFromDatabase == null) {
            fail()
            return
        }

        for (articleFromDatabase in rssFromDatabase.articles) {
            assertTrue(articleFromDatabase in articles)
        }
    }

    @Test
    fun setZeroMaxValuesIfTryToSetLessThanZero() {
        val repository = RssRepository(mBoxStore, -1, -1)

        assertEquals(0, repository.maxRssCount)
        assertEquals(0, repository.maxArticlesInRssCount)
    }

    @Test
    fun returnFullStateIfTryToAddRssToFullRepository() {
        val repository = RssRepository(mBoxStore, 0, 0)

        assertEquals(IRepository.InsertRssResult.FULL, repository.putNewRss(mRss))
    }

    @Test
    fun takeOnlyMaxArticlesCountFromRss() {
        val maxArticlesCount = 10
        val repository = RssRepository(mBoxStore, 1, maxArticlesCount)
        val rss = Rss()
        rss.articles.addAll(Array(maxArticlesCount * 2) {
            createArticle(it)
        })

        assertEquals(IRepository.InsertRssResult.SUCCESS, repository.putNewRss(rss))
        assertEquals(maxArticlesCount, repository.getRssById(rss.id)?.articles?.count())
    }

    @Test
    fun returnTrueIfRssCountIsMax() {
        val alwaysFullRepository = RssRepository(mBoxStore, 0, 0)

        assertTrue(alwaysFullRepository.isFull)

        val count = 10
        val fullRssRepository = RssRepository(mBoxStore, count, 0)
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
        val bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8)
        val imageUrl = "Image url"
        val rss = Rss()
        rss.articles.add(Article(imageUrl = imageUrl))

        assertEquals(IRepository.InsertRssResult.SUCCESS, mRepository.putNewRss(rss))
        assertNotNull(mRepository.getRssById(rss.id))

        val articleId = rss.articles[0].id
        val putImageResult = mRepository.putArticleImageIfArticleExist(articleId, bitmap)

        assertTrue(putImageResult)
        assertNotNull(mRepository.getArticleById(articleId))
        assertNotNull(mRepository.getArticleImageByArticleId(articleId))
        assertNotNull(mRepository.getArticleImageUrlByArticleId(articleId))
        assertNotNull(mRepository.getArticleImageBitmapByArticleId(articleId))

        assertTrue(mRepository.removeRssById(rss.id))

        assertNull(mRepository.getRssById(rss.id))
        assertNull(mRepository.getArticleById(articleId))
        assertNull(mRepository.getArticleImageByArticleId(articleId))
        assertNull(mRepository.getArticleImageUrlByArticleId(articleId))
        assertNull(mRepository.getArticleImageBitmapByArticleId(articleId))
    }

    @Test
    fun notPutArticleImageIfArticleNotExist() {
        val bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8)
        val insertionResult = mRepository.putArticleImageIfArticleExist(10, bitmap)

        assertFalse(insertionResult)
        assertEquals(0, mBoxStore.boxFor<ArticleImage>().count())
    }

    private fun createArticle(number: Int): Article {
        return Article(
                title = ARTICLE_TITLE.format(number),
                description = ARTICLE_DESCRIPTION.format(number),
                originUrl = ARTICLE_ORIGIN.format(number))
    }
}