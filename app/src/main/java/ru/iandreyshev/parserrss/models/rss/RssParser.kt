package ru.iandreyshev.parserrss.models.rss

import java.util.ArrayList

import ru.iandreyshev.parserrss.models.repository.Rss

class RssParser {
    companion object {
        private val PARSERS = ArrayList<RssParseEngine>()

        init {
            PARSERS.add(RssParserV2())
        }

        @JvmStatic
        fun parse(rssText: String): Rss? {
            PARSERS.forEach { parser ->
                val rss = parser.parse(rssText)

                rss?.let { return rss }
            }

            return null
        }
    }
}
