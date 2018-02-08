package ru.iandreyshev.parserrss.models.repository

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.robolectric.RobolectricTestRunner

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

        private const val ARTICLES_COUNT = 25
        private const val ARTICLE_TITLE = "TITLE %s"
        private const val ARTICLE_DESCRIPTION = "DESCRIPTION %s"
        private const val ARTICLE_ORIGIN = "ORIGIN %s"
    }

    private lateinit var mRepository: RssRepository
    private lateinit var mRss: Rss

    @Before
    fun setup() {
        val tempFile = File.createTempFile("object-store-test", "")

        assertTrue(tempFile.delete())

        mRepository = RssRepository(MyObjectBox.builder().directory(tempFile).build())

        mRss = Rss(
                title = RSS_TITLE,
                origin = RSS_ORIGIN,
                url = RSS_URL)
        val articleList = ArrayList<Article>()

        repeat(ARTICLES_COUNT) {
            articleList.add(createArticle(it))
        }

        mRss.articles = articleList
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

        assertEquals(IRepository.PutRssState.SUCCESS, mRepository.putNewRss(newRss))
        assertTrue(mRepository.updateRssWithSameUrl(mRss))

        assertNotEquals(newRss.title, mRss.title)
        assertNotEquals(newRss.description, mRss.description)
    }

    @Test
    fun returnFalseWhenUpdatingNotExistRss() {
        val newRss = Rss(title = "", origin = "", url = NOT_USE_RSS_URL)

        assertEquals(IRepository.PutRssState.SUCCESS, mRepository.putNewRss(newRss))
        assertFalse(mRepository.updateRssWithSameUrl(mRss))
    }

    @Test
    fun removeRss() {
        assertEquals(IRepository.PutRssState.SUCCESS, mRepository.putNewRss(mRss))
        mRepository.removeRssById(mRss.id)
        assertNull(mRepository.getRssById(mRss.id))
    }

    @Test
    fun returnExistStateAfterPutRssWithSameUrl() {
        mRepository.putNewRss(mRss)
        val rssWithSameUrl = Rss(title = "", origin = "", url = RSS_URL)
        rssWithSameUrl.url = mRss.url
        assertEquals(IRepository.PutRssState.EXIST, mRepository.putNewRss(rssWithSameUrl))
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

    private fun createArticle(number: Int): Article {
        return Article(
                title = ARTICLE_TITLE.format(number),
                description = ARTICLE_DESCRIPTION.format(number),
                originUrl = ARTICLE_ORIGIN.format(number))
    }
}