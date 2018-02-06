package ru.iandreyshev.parserrss.models.filters

import ru.iandreyshev.parserrss.models.repository.Article

class ArticlesFilterByDate {
    companion object : IArticlesFilter {
        override fun sort(articles: MutableList<Article>) {
            articles.sortByDescending { it -> it.date }
        }
    }
}
