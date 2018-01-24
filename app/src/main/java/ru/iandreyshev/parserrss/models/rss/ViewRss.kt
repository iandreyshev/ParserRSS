package ru.iandreyshev.parserrss.models.rss

import java.util.ArrayList

import ru.iandreyshev.parserrss.models.repository.Article
import ru.iandreyshev.parserrss.models.repository.Rss

class ViewRss(rss: Rss) {
    val id: Long = rss.id
    val title: String = rss.title
    val description: String = rss.description
    val url: String = rss.url
    val origin: String = rss.origin

    private val mArticles: MutableList<ViewArticle> = ArrayList()

    val viewArticles: List<ViewArticle>
        get() = mArticles

    init {
        rss.articles.forEach { article -> mArticles.add(ViewArticle(article)) }
    }
}
