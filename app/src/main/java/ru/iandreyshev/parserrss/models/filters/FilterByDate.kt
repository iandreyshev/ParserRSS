package ru.iandreyshev.parserrss.models.filters

import ru.iandreyshev.parserrss.models.repository.Article

class FilterByDate : IArticlesFilter {
    companion object {
        fun newInstance(): FilterByDate {
            return FilterByDate()
        }
    }

    override fun sort(articles: List<Article>): List<Article> {
        return articles.sortedByDescending { it -> it.date }
    }
}
