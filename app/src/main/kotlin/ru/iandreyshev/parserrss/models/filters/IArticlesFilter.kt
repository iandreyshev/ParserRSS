package ru.iandreyshev.parserrss.models.filters

import ru.iandreyshev.parserrss.models.rss.Article

interface IArticlesFilter {
    fun sort(articles: MutableList<Article>)
}
