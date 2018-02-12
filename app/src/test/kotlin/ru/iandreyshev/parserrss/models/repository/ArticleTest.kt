package ru.iandreyshev.parserrss.models.repository

import org.junit.Before
import org.junit.Test

import java.util.Date

import org.junit.Assert.*

class ArticleTest {

    companion object {
        private const val TITLE = "TITLE"
        private const val DESCRIPTION = "DESCRIPTION"
        private const val ORIGIN = "ORIGIN"
        private const val RSS_ID = 1L
        private const val IMAGE_URL = "IMAGE_URL"
        private val POST_DATE = Date()
    }

    private lateinit var mArticle: ArticleEntity
    private lateinit var mDefaultArticle: ArticleEntity

    @Before
    fun setup() {
        mArticle = ArticleEntity(
                title = TITLE,
                description = DESCRIPTION,
                originUrl = ORIGIN)
        mDefaultArticle = ArticleEntity(
                title = "",
                description = "",
                originUrl = "")
    }

    @Test
    fun haveEmptyTitleByDefault() {
        assertEquals(mDefaultArticle.title, "")
    }

    @Test
    fun haveEmptyDescriptionByDefault() {
        assertEquals(mDefaultArticle.description, "")
    }

    @Test
    fun haveEmptyOriginByDefault() {
        assertEquals(mDefaultArticle.originUrl, "")
    }

    @Test
    fun haveConstructorWithZeroArgs() {
        val article = ArticleEntity(title = "", description = "", originUrl = "")

        assertNotNull(article)
    }

    @Test
    fun haveIdGetter() {
        assertEquals(mArticle.id, 0)
    }

    @Test
    fun haveRssIdGetterAndSetter() {
        mArticle.rssId = RSS_ID

        assertEquals(mArticle.rssId, RSS_ID)
    }

    @Test
    fun haveImageUrlGetterAndSetter() {
        mArticle.imageUrl = IMAGE_URL

        assertEquals(mArticle.imageUrl, IMAGE_URL)
    }

    @Test
    fun haveDateGetterAndSetter() {
        mArticle.date = POST_DATE.time

        assertEquals(mArticle.date ?: 0, POST_DATE.time)
    }

    @Test
    fun equalWithObjectIfEqualsItsOrigins() {
        var other = ArticleEntity(title = TITLE, description = DESCRIPTION, originUrl = "")

        assertFalse(other == mArticle)

        other = ArticleEntity(title = "", description = "", originUrl = mArticle.originUrl)

        assertTrue(mArticle == other)
    }

    @Test
    fun hashcodeEqualOriginHashcode() {
        val originUrlHashCode = mArticle.originUrl?.hashCode()?.toLong()
        val articleHashCode = mArticle.hashCode().toLong()

        assertEquals(articleHashCode, originUrlHashCode)
    }
}