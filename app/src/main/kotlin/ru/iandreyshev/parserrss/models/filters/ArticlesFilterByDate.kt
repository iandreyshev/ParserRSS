package ru.iandreyshev.parserrss.models.filters

import ru.iandreyshev.parserrss.models.rss.Article

class ArticlesFilterByDate : IArticlesFilter {
    override fun sort(articles: MutableList<Article>) {
        articles.sortByDescending { it -> it.date }
    }
}
