package ru.iandreyshev.parserrss.models.filters

import ru.iandreyshev.parserrss.models.repository.Article

interface IArticlesFilter {
    fun sort(articles: MutableList<Article>): MutableList<Article>
}
