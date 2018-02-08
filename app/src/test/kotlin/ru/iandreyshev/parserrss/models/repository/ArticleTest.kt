package ru.iandreyshev.parserrss.models.repository

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner

import java.util.Date

import org.junit.Assert.*
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = "src/main/AndroidManifest.xml")
class ArticleTest {

    companion object {
        private const val TITLE = "TITLE"
        private const val DESCRIPTION = "DESCRIPTION"
        private const val ORIGIN = "ORIGIN"
        private const val RSS_ID = 1L
        private const val IMAGE_URL = "IMAGE_URL"
        private val POST_DATE = Date()
    }

    private lateinit var mArticle: Article
    private lateinit var mDefaultArticle: Article

    @Before
    fun setup() {
        mArticle = Article(
                title = TITLE,
                description = DESCRIPTION,
                originUrl = ORIGIN)
        mDefaultArticle = Article(
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
        val article = Article(title = "", description = "", originUrl = "")

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
        var other = Article(title = TITLE, description = DESCRIPTION, originUrl = "")

        assertFalse(other == mArticle)

        other = Article(title = "", description = "", originUrl = mArticle.originUrl)

        assertTrue(mArticle == other)
    }

    @Test
    fun hashcodeEqualOriginHashcode() {
        assertEquals(mArticle.hashCode().toLong(), mArticle.originUrl.hashCode().toLong())
    }
}