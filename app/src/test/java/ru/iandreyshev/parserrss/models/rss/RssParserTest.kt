package ru.iandreyshev.parserrss.models.rss

import org.jdom2.Element
import org.junit.Before
import org.junit.Test

import java.util.ArrayList

import ru.iandreyshev.parserrss.TestUtils
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.repository.Article

import org.junit.Assert.*

class RssParserTest {
    private lateinit var feedParseExceptionParser: RssParseEngine
    private lateinit var articlesParseExceptionParser: RssParseEngine

    @Before
    fun setup() {
        feedParseExceptionParser = object : RssParseEngine() {
            override fun parseRss(root: Element): Rss? {
                throw UnsupportedOperationException()
            }

            override fun parseArticles(root: Element): ArrayList<Article> {
                fail()

                return ArrayList()
            }
        }
        articlesParseExceptionParser = object : RssParseEngine() {
            override fun parseRss(root: Element): Rss? {
                return null
            }

            override fun parseArticles(root: Element): ArrayList<Article> {
                throw UnsupportedOperationException()
            }
        }
    }

    @Test
    fun subclassesCanThrowExceptionInFeedParsingMethod() {
        val rss = feedParseExceptionParser.parse(getXml("root_only"))

        assertNull(rss)
    }

    @Test
    fun subclassesCanThrowExceptionInArticlesParsingMethod() {
        val rss = articlesParseExceptionParser.parse(getXml("root_only"))

        assertNull(rss)
    }

    private fun getXml(fileName: String): String? {
        return TestUtils.readFromFile("/xmlSamples/xml/$fileName.xml")
    }
}