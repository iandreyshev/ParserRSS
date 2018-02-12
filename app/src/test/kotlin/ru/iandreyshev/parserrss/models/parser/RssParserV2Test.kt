package ru.iandreyshev.parserrss.models.parser

import org.junit.Before
import org.junit.Test

import ru.iandreyshev.parserrss.TestUtils

import org.junit.Assert.*
import ru.iandreyshev.parserrss.models.rss.Rss

class RssParserV2Test {
    companion object {
        private const val RSS_TITLE = "Feed title"
        private const val RSS_DESCRIPTION = "Feed description"
        private const val ARTICLE_TITLE = "Article title"
        private const val ARTICLE_TEXT = "Article text"
        private const val ARTICLE_IMG_URL = "Article_image_url"
    }

    private lateinit var mParser: ParserV2

    @Before
    fun setup() {
        mParser = ParserV2()
    }

    @Test
    fun parseRssWithoutXmlFormat() {
        val rss = parseFile("valid_without_xml_format")

        assertEquals(RSS_TITLE, rss.title)
        assertEquals(RSS_DESCRIPTION, rss.description)
        assertNotNull(rss.originUrl)
    }

    @Test
    fun parseRssWithFeedTitleAndDescriptionOnly() {
        val rss = parseFile("valid_minimal")

        assertEquals(RSS_TITLE, rss.title)
        assertEquals(RSS_DESCRIPTION, rss.description)
    }

    @Test
    fun notParseIfTitleIsAbsent() {
        try {
            parseFile("invalid_without_title")
        } catch (ex: Exception) {
            return
        }

        fail()
    }

    @Test
    fun returnEmptyArticlesListIfArticlesIsAbsent() {
        val rss = parseFile("valid_minimal")

        assertTrue(rss.articles.isEmpty())
    }

    @Test
    fun notParseArticlesInItemsNode() {
        val rss = parseFile("invalid_with_articles_in_items_node")

        assertTrue(rss.articles.isEmpty())
    }

    @Test
    fun parseArticlesInChannelNodeIfTheyHaveTitleLinkDescription() {
        val rss = parseFile("valid_with_articles")

        assertEquals(2, rss.articles.size.toLong())
        rss.articles.forEach {
            assertEquals(ARTICLE_TITLE, it.title)
            assertEquals(ARTICLE_TEXT, it.description)
            assertNotNull(it.originUrl)
        }
    }

    @Test
    fun parseArticlesWithImageEnclosure() {
        val rss = parseFile("valid_with_article_image")

        assertEquals(2, rss.articles.size.toLong())
        rss.articles.forEach { assertEquals(ARTICLE_IMG_URL, it.imageUrl) }
    }

    @Test
    fun returnNullIfParseInvalidXml() {
        try {
            parseFile("invalid_with_bad_xml")
        } catch (ex: Exception) {
            return
        }

        fail()
    }

    @Test
    fun returnNullIfParseNullString() {
        val rss = mParser.parse(null)

        assertNull(rss)
    }

    @Test
    fun returnEmptyArticleListIfTheyDoNotHaveTitleOrDescriptionOrLink() {
        val rss = parseFile("valid_without_one_of_required_article_element")

        assertTrue(rss.articles.isEmpty())
    }

    @Test
    fun notParseArticlesImageIfTheyDoNotHaveUrlOrType() {
        val rss = parseFile("valid_with_articles_enclosure_without_required_element")
        rss.articles.forEach { article -> assertNull(article.imageUrl) }
    }

    @Test
    fun parseArticleDate() {
        val rss = parseFile("valid_with_pub_date")

        assertNotNull(rss.articles[0].date)
    }

    private fun parseFile(fileName: String): Rss {
        val data = TestUtils.readFromFile(toPath(fileName))
        return mParser.parse(data) ?: throw IllegalArgumentException()
    }

    private fun toPath(fileName: String): String {
        return "/xmlSamples/v2_0/$fileName.xml"
    }
}