package ru.iandreyshev.parserrss.models.repository

import org.junit.Before
import org.junit.Test

import java.util.ArrayList

import org.junit.Assert.*

class RssTest {

    companion object {
        private const val TITLE = "Title"
        private const val ORIGIN = "Origin"
        private const val DESCRIPTION = "Description"
        private const val URL = "URL"
        private const val ID: Long = 12
        private val ARTICLE = Article(title = TITLE, description = DESCRIPTION, originUrl = ORIGIN)
    }

    private lateinit var rss: Rss
    private lateinit var defaultRss: Rss

    @Before
    fun setup() {
        rss = Rss(title = TITLE, origin = ORIGIN)
        defaultRss = Rss(title = "", origin = "")
    }

    @Test
    fun haveIdGetterAndSetter() {
        rss.id = ID

        assertEquals(rss.id, ID)
    }

    @Test
    fun haveTitleGetter() {
        assertEquals(rss.title, TITLE)
    }

    @Test
    fun haveDescriptionGetterAndSetter() {
        rss.description = DESCRIPTION

        assertEquals(rss.description, DESCRIPTION)
    }

    @Test
    fun haveEmptyTitleByDefault() {
        assertEquals(defaultRss.title, "")
    }

    @Test
    fun haveEmptyUrlByDefault() {
        assertEquals(defaultRss.url, "")
    }

    @Test
    fun haveEmptyOriginByDefault() {
        assertEquals(defaultRss.origin, "")
    }

    @Test
    fun haveEmptyListOfArticlesByDefault() {
        assertEquals(defaultRss.articles.size.toLong(), 0)
    }

    @Test
    fun haveEmptyListOfArticlesViewsByDefault() {
        assertTrue(defaultRss.articles.isEmpty())
    }

    @Test
    fun haveArticlesGetterAndSetter() {
        val articles = ArrayList<Article>()
        articles.add(ARTICLE)
        rss.articles = articles

        assertEquals(rss.articles[0], ARTICLE)
    }

    @Test
    fun haveUrlGetterAndSetter() {
        rss.url = URL

        assertEquals(rss.url, URL)
    }

    @Test
    fun hashcodeEqualsUrlHashcode() {
        rss.url = URL

        assertEquals(rss.hashCode().toLong(), URL.hashCode().toLong())
    }

    @Test
    fun equalsWithObjectIfEqualsItsUrls() {
        rss.url = URL
        val otherRss = Rss(title = "", origin = "")

        assertFalse(rss == otherRss)

        otherRss.url = URL

        assertTrue(rss == otherRss)
    }
}