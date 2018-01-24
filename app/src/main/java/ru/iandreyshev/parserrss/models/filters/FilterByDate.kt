package ru.iandreyshev.parserrss.models.filters

import java.util.TreeSet

import ru.iandreyshev.parserrss.models.repository.Article

class FilterByDate : IArticlesFilter {
    companion object {
        const val EQUALS = 0
        const val LESS = -1
        const val GREATER = 1

        @JvmStatic
        fun newInstance(): FilterByDate {
            return FilterByDate()
        }
    }

    override fun sort(articles: List<Article>): List<Article> {
        val sortedSet = TreeSet { right: Article, left: Article ->
            if (right.date == null && left.date == null) {
                return@TreeSet EQUALS
            } else if (right.date == null) {
                return@TreeSet LESS
            } else if (left.date == null) {
                return@TreeSet GREATER
            }

            left.date!!.compareTo(right.date)
        }

        sortedSet.addAll(articles)

        return sortedSet.toList()
    }
}
