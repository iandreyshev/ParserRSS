package ru.iandreyshev.parserrss.models.rss

import org.jdom2.Element

import java.util.ArrayList

import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.repository.Article
import ru.iandreyshev.parserrss.models.rss.extension.toDocument

internal abstract class ParserEngine {
    companion object {
        private val PARSERS = ArrayList<ParserEngine>()

        init {
            PARSERS.add(ParserV2())
        }

        fun parse(rssText: String?, maxArticlesCount: Int): Rss? {
            PARSERS.forEach { it.parse(rssText, maxArticlesCount)?.let { return it } }

            return null
        }
    }

    fun parse(rssText: String?, maxArticlesCount: Int = Int.MAX_VALUE): Rss? {
        return try {
            val root = rssText?.toDocument()?.rootElement ?: return null
            val rss = parseRss(root) ?: return null
            val articleNodes = getArticlesNodes(root)

            articleNodes?.forEachIndexed { index, element ->
                if (index >= maxArticlesCount) return@forEachIndexed
                parseArticle(element)?.let { rss.articles.add(it) }
            }

            rss

        } catch (ex: Exception) {
            null
        }
    }

    protected abstract fun parseRss(root: Element): Rss?

    protected abstract fun getArticlesNodes(root: Element): List<Element>?

    protected abstract fun parseArticle(node: Element): Article?
}
