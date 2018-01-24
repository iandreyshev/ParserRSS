package ru.iandreyshev.parserrss.models.rss

import org.jdom2.Element

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale

import ru.iandreyshev.parserrss.models.repository.Article
import ru.iandreyshev.parserrss.models.repository.Rss

internal class RssParserV2 : RssParseEngine() {

    companion object {
        private const val FEED_NAME = "channel"
        private const val FEED_TITLE = "title"
        private const val FEED_ORIGIN = "link"
        private const val FEED_DESCRIPTION = "description"

        private const val ARTICLE_NAME = "item"
        private const val ARTICLE_TITLE = "title"
        private const val ARTICLE_ORIGIN = "link"
        private const val ARTICLE_DESCRIPTION = "description"

        private const val DATE_NAME = "pubDate"
        private val DATE_FORMAT = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH)

        private const val ARTICLE_IMG_NODE = "enclosure"
        private const val ARTICLE_IMG_URL = "url"
        private const val ARTICLE_IMG_TYPE = "type"

        private val IMAGE_TYPES = setOf(MimeType.JPEG, MimeType.PNG)
    }

    override fun parseRss(root: Element): Rss? {
        val channel = root.getChild(FEED_NAME)
        val title = channel.getChildText(FEED_TITLE)
        val origin = channel.getChildText(FEED_ORIGIN)

        if (title == null || origin == null) {
            return null
        }

        val rss = Rss(title, origin)
        rss.description = channel.getChildText(FEED_DESCRIPTION)

        return rss
    }

    override fun parseArticles(root: Element): ArrayList<Article> {
        val channel = root.getChild(FEED_NAME)
        val items = channel.getChildren(ARTICLE_NAME)
        val result = ArrayList<Article>()

        items.forEach { item ->
            val article = parseArticle(item)

            if (article != null) {
                result.add(article)
            }
        }

        return result
    }

    private fun parseArticle(item: Element): Article? {
        val title = item.getChildText(ARTICLE_TITLE)
        val origin = item.getChildText(ARTICLE_ORIGIN)
        val description = item.getChildText(ARTICLE_DESCRIPTION)

        if (title == null || origin == null || description == null) {
            return null
        }

        val article = Article(title, description, origin)
        parseArticleDate(item, article)
        parseArticleImage(item, article)

        return article
    }

    private fun parseArticleDate(item: Element, article: Article) {
        val dateStr = item.getChildText(DATE_NAME)
        val date: Date

        try {
            date = DATE_FORMAT.parse(dateStr)
        } catch (ex: Exception) {
            return
        }

        article.setDate(date)
    }

    private fun parseArticleImage(item: Element, article: Article) {
        val resource = item.getChild(ARTICLE_IMG_NODE) ?: return

        val url = resource.getAttributeValue(ARTICLE_IMG_URL)
        val type = resource.getAttributeValue(ARTICLE_IMG_TYPE)

        if (url == null || !IMAGE_TYPES.contains(type)) {
            return
        }

        article.imageUrl = url
    }
}
