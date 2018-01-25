package ru.iandreyshev.parserrss.models.rss

import org.jdom2.Document
import org.jdom2.Element
import org.jdom2.input.SAXBuilder

import java.io.StringReader
import java.util.ArrayList

import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.repository.Article

internal abstract class RssParseEngine {
    companion object {
        private const val DISABLE_DTD_FEATURE = "http://apache.org/xml/features/nonValidating/load-external-dtd"
        private val DOC_BUILDERS = ArrayList<SAXBuilder>()

        init {
            val withoutDtdBuilder = SAXBuilder()
            withoutDtdBuilder.setFeature(DISABLE_DTD_FEATURE, false)
            DOC_BUILDERS.add(withoutDtdBuilder)
            DOC_BUILDERS.add(SAXBuilder())
        }

        private fun toDocument(xmlText: String?): Document? {
            for (builder in DOC_BUILDERS) {
                try {

                    return builder.build(StringReader(xmlText)) ?: continue

                } catch (ex: Exception) {
                    continue
                }
            }

            return null
        }
    }

    fun parse(rssText: String?): Rss? {
        try {
            val doc = toDocument(rssText) ?: return null

            val root = doc.rootElement
            val rss = parseRss(root) ?: return null

            rss.articles = parseArticles(root)

            return rss

        } catch (ex: Exception) {
            return null
        }

    }

    protected abstract fun parseRss(root: Element): Rss?

    protected abstract fun parseArticles(root: Element): ArrayList<Article>
}
