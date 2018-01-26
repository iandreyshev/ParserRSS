package ru.iandreyshev.parserrss.models.rss

import org.jdom2.Element

import java.util.ArrayList

import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.repository.Article
import ru.iandreyshev.parserrss.models.rss.extension.toDocument

internal abstract class RssParseEngine {
    companion object {
        private val PARSERS = ArrayList<RssParseEngine>()

        init {
            PARSERS.add(RssParserV2())
        }

        fun parse(rssText: String?): Rss? {
            PARSERS.forEach { it.parse(rssText)?.let { return it }}
            return null
        }
    }

    fun parse(rssText: String?): Rss? {
        return try {
            val root = rssText?.toDocument()?.rootElement ?: return null
            val rss = parseRss(root) ?: return null

            rss.articles = parseArticles(root)

            rss

        } catch (ex: Exception) {
            null
        }
    }

    protected abstract fun parseRss(root: Element): Rss?

    protected abstract fun parseArticles(root: Element): ArrayList<Article>
}
