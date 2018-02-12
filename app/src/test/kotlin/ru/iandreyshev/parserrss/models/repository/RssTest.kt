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
        private val ARTICLE = ArticleEntity(title = TITLE, description = DESCRIPTION, originUrl = ORIGIN)
    }

    private lateinit var mRss: RssEntity
    private lateinit var mDefaultRss: RssEntity

    @Before
    fun setup() {
        mRss = RssEntity(title = TITLE, originUrl = ORIGIN)
        mDefaultRss = RssEntity(title = "", originUrl = "")
    }

    @Test
    fun haveIdGetterAndSetter() {
        mRss.id = ID

        assertEquals(mRss.id, ID)
    }

    @Test
    fun haveTitleGetter() {
        assertEquals(mRss.title, TITLE)
    }

    @Test
    fun haveDescriptionGetterAndSetter() {
        mRss.description = DESCRIPTION

        assertEquals(mRss.description, DESCRIPTION)
    }

    @Test
    fun haveEmptyTitleByDefault() {
        assertEquals(mDefaultRss.title, "")
    }

    @Test
    fun haveEmptyUrlByDefault() {
        assertEquals(mDefaultRss.url, "")
    }

    @Test
    fun haveEmptyOriginByDefault() {
        assertEquals(mDefaultRss.originUrl, "")
    }

    @Test
    fun haveEmptyListOfArticlesByDefault() {
        assertEquals(mDefaultRss.articles.size.toLong(), 0)
    }

    @Test
    fun haveEmptyListOfArticlesViewsByDefault() {
        assertTrue(mDefaultRss.articles.isEmpty())
    }

    @Test
    fun haveArticlesGetterAndSetter() {
        val articles = ArrayList<ArticleEntity>()
        articles.add(ARTICLE)
        mRss.articles = articles

        assertEquals(mRss.articles[0], ARTICLE)
    }

    @Test
    fun haveUrlGetterAndSetter() {
        mRss.url = URL

        assertEquals(mRss.url, URL)
    }

    @Test
    fun hashcodeEqualsUrlHashcode() {
        mRss.url = URL

        assertEquals(mRss.hashCode().toLong(), URL.hashCode().toLong())
    }

    @Test
    fun equalsWithObjectIfEqualsItsUrls() {
        mRss.url = URL
        val otherRss = RssEntity(title = "", originUrl = "")

        assertFalse(mRss == otherRss)

        otherRss.url = URL

        assertTrue(mRss == otherRss)
    }
}