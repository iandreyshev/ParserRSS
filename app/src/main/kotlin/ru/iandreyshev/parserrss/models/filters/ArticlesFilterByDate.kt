package ru.iandreyshev.parserrss.models.filters

import ru.iandreyshev.parserrss.models.repository.Article

class ArticlesFilterByDate {
    companion object : IArticlesFilter {
        val newInstance = ArticlesFilterByDate()

        override fun sort(articles: MutableList<Article>) {
            return articles.sortByDescending { it -> it.date }
        }
    }
}
