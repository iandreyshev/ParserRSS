package ru.iandreyshev.parserrss.models.parser

import android.os.Build
import android.text.Html
import org.jdom2.Element

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import ru.iandreyshev.parserrss.models.repository.Article
import ru.iandreyshev.parserrss.models.repository.Rss

class ParserV2 : RssParser() {

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

        val rss = Rss(
                title = clearHtml(title),
                origin = origin)

        val description = channel.getChildText(FEED_DESCRIPTION)

        if (description != null) {
            rss.description = clearHtml(description)
        }

        return rss
    }

    override fun getArticlesNodes(root: Element): List<Element>? {
        return root.getChild(FEED_NAME).getChildren(ARTICLE_NAME)
    }

    override fun parseArticle(node: Element): Article? {
        val title = node.getChildText(ARTICLE_TITLE)
        val origin = node.getChildText(ARTICLE_ORIGIN)
        val description = node.getChildText(ARTICLE_DESCRIPTION)

        if (title == null || origin == null || description == null) {
            return null
        }

        val article = Article(
                title = clearHtml(title),
                description = clearHtml(description),
                originUrl = origin)

        parseArticleDate(node, article)
        parseArticleImage(node, article)

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

        article.date = date.time
    }

    private fun parseArticleImage(item: Element, article: Article) {
        val resource = item.getChild(ARTICLE_IMG_NODE) ?: return
        val url = resource.getAttributeValue(ARTICLE_IMG_URL) ?: return
        val type = resource.getAttributeValue(ARTICLE_IMG_TYPE) ?: return

        if (type !in IMAGE_TYPES) {
            return
        }

        article.imageUrl = url
    }

    private fun clearHtml(html: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString()
        } else {
            Html.fromHtml(html).toString()
        }
    }
}
