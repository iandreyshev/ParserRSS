package ru.iandreyshev.parserrss.models.repository

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

import java.util.Date

import org.junit.Assert.*

@RunWith(RobolectricTestRunner::class)
class ArticleTest {
    companion object {
        private const val TITLE = "TITLE"
        private const val DESCRIPTION = "DESCRIPTION"
        private const val ORIGIN = "ORIGIN"
        private const val RSS_ID = 1L
        private const val IMAGE_URL = "IMAGE_URL"
        private val POST_DATE = Date()
    }

    private lateinit var article: Article
    private lateinit var defaultArticle: Article

    @Before
    fun setup() {
        article = Article(title = TITLE, description = DESCRIPTION, originUrl = ORIGIN)
        defaultArticle = Article(title = "", description = "", originUrl = "")
    }

    @Test
    fun haveEmptyTitleByDefault() {
        assertEquals(defaultArticle.title, "")
    }

    @Test
    fun haveEmptyDescriptionByDefault() {
        assertEquals(defaultArticle.description, "")
    }

    @Test
    fun haveEmptyOriginByDefault() {
        assertEquals(defaultArticle.originUrl, "")
    }

    @Test
    fun haveConstructorWithZeroArgs() {
        val article = Article(title = "", description = "", originUrl = "")

        assertNotNull(article)
    }

    @Test
    fun haveIdGetter() {
        assertEquals(article.id, 0)
    }

    @Test
    fun haveRssIdGetterAndSetter() {
        article.rssId = RSS_ID

        assertEquals(article.rssId, RSS_ID)
    }

    @Test
    fun haveImageUrlGetterAndSetter() {
        article.imageUrl = IMAGE_URL

        assertEquals(article.imageUrl, IMAGE_URL)
    }

    @Test
    fun haveDateGetterAndSetter() {
        article.date = POST_DATE.time

        assertEquals(article.date ?: 0, POST_DATE.time)
    }

    @Test
    fun equalWithObjectIfEqualsItsOrigins() {
        var other = Article(title = TITLE, description = DESCRIPTION, originUrl = "")

        assertFalse(other == article)

        other = Article(title = "", description = "", originUrl = article.originUrl)

        assertTrue(article == other)
    }

    @Test
    fun hashcodeEqualOriginHashcode() {
        assertEquals(article.hashCode().toLong(), article.originUrl.hashCode().toLong())
    }
}