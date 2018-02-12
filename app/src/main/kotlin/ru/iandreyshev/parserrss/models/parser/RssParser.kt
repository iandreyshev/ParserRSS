package ru.iandreyshev.parserrss.models.parser

import org.jdom2.Element

import ru.iandreyshev.parserrss.models.parser.extension.toDocument
import ru.iandreyshev.parserrss.models.rss.Article
import ru.iandreyshev.parserrss.models.rss.Rss

abstract class RssParser {

    open fun parse(rssText: String?, maxArticlesCount: Int = Int.MAX_VALUE): Rss? {
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
