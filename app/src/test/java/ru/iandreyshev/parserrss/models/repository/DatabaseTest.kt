package ru.iandreyshev.parserrss.models.repository

import junit.framework.Assert

import junit.framework.TestCase

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

import org.junit.Assert.*

import java.io.File
import java.util.ArrayList
import java.util.HashSet

@RunWith(RobolectricTestRunner::class)
class DatabaseTest : TestCase() {

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

    private lateinit var database: Database
    private lateinit var rss: Rss

    @Before
    @Throws(Exception::class)
    fun setup() {
        val tempFile = File.createTempFile("object-store-test", "")

        assertTrue(tempFile.delete())

        database = Database(MyObjectBox.builder().directory(tempFile).build())

        rss = Rss(
                title = RSS_TITLE,
                origin = RSS_ORIGIN,
                url = RSS_URL)
        val articleList = ArrayList<Article>()

        repeat(ARTICLES_COUNT) {
            articleList.add(createArticle(it))
        }

        rss.articles = articleList
    }

    @Test
    fun noThrowExceptionIfTryToGetItemByInvalidId() {
        try {
            Database.INVALID_IDS.forEach {
                database.getArticleById(it)
                database.getRssById(it)
                database.removeRssById(it)
            }
        } catch (ex: Exception) {
            Assert.fail()
        }
    }

    @Test
    fun returnNullIfGetRssByInvalidId() {
        Assert.assertNull(database.getRssById(-2))
    }

    @Test
    @Throws(Exception::class)
    fun returnTrueIfRssWithSameUrlExist() {
        database.putRssIfSameUrlNotExist(rss)

        Assert.assertTrue(database.isRssWithUrlExist(RSS_URL))
    }

    @Test
    @Throws(Exception::class)
    fun returnFalseIfRssWithSameUrlNotExist() {
        database.putRssIfSameUrlNotExist(rss)

        Assert.assertFalse(database.isRssWithUrlExist(NOT_USE_RSS_URL))
    }

    @Test
    @Throws(Exception::class)
    fun returnArticlesById() {
        database.putRssIfSameUrlNotExist(rss)

        for ((id) in rss.articles) {
            Assert.assertNotNull(database.getArticleById(id))
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateRssIdAfterPut() {
        database.putRssIfSameUrlNotExist(rss)

        Assert.assertEquals(database.getRssById(rss.id), rss)
    }

    @Test
    @Throws(Exception::class)
    fun returnTrueWhenUpdatingRssExist() {
        val newRss = Rss(title = "Title", origin = "Origin")
        newRss.description = "Description"
        newRss.url = rss.url

        assertNotEquals(newRss.title, rss.title)
        assertNotEquals(newRss.description, rss.description)
        assertNotEquals(newRss.articles.size.toLong(), rss.articles.size.toLong())

        Assert.assertEquals(newRss.url, rss.url)

        Assert.assertTrue(database.putRssIfSameUrlNotExist(newRss))
        Assert.assertTrue(database.updateRssWithSameUrl(rss))

        assertNotEquals(newRss.title, rss.title)
        assertNotEquals(newRss.description, rss.description)
    }

    @Test
    @Throws(Exception::class)
    fun returnFalseWhenUpdatingNotExistRss() {
        val newRss = Rss(title = "", origin = "", url = NOT_USE_RSS_URL)

        Assert.assertTrue(database.putRssIfSameUrlNotExist(newRss))
        Assert.assertFalse(database.updateRssWithSameUrl(rss))
    }

    @Test
    @Throws(Exception::class)
    fun removeRss() {
        assertTrue(database.putRssIfSameUrlNotExist(rss))

        database.removeRssById(rss.id)

        assertNull(database.getRssById(rss.id))
    }

    @Test
    @Throws(Exception::class)
    fun returnFalseAfterPutRssWithSameUrl() {
        database.putRssIfSameUrlNotExist(rss)

        val rssWithSameUrl = Rss(title = "", origin = "", url = RSS_URL)
        rssWithSameUrl.url = rss.url

        Assert.assertFalse(database.putRssIfSameUrlNotExist(rssWithSameUrl))
    }

    @Test
    @Throws(Exception::class)
    fun updateRssArticlesIdAfterPut() {
        database.putRssIfSameUrlNotExist(rss)

        for ((_, rssId) in rss.articles) {
            Assert.assertTrue(rssId == rss.id)
        }
    }

    @Test
    @Throws(Exception::class)
    fun returnRssWithSameArticles() {
        val articles = HashSet(rss.articles)

        database.putRssIfSameUrlNotExist(rss)
        val rssFromDatabase = database.getRssById(rss.id)

        if (rssFromDatabase == null) {
            fail()
            return
        }

        for (articleFromDatabase in rssFromDatabase.articles) {
            Assert.assertTrue(articles.contains(articleFromDatabase))
        }
    }

    private fun createArticle(number: Int): Article {
        return Article(
                title = ARTICLE_TITLE.format(number),
                description = ARTICLE_DESCRIPTION.format(number),
                originUrl = ARTICLE_ORIGIN.format(number))
    }
}