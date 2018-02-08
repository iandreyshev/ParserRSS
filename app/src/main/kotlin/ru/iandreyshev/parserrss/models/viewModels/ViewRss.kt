package ru.iandreyshev.parserrss.models.viewModels

import java.util.ArrayList

import ru.iandreyshev.parserrss.models.repository.Rss

class ViewRss(
        var id: Long = 0,
        var title: String? = null,
        var url: String? = null,
        var description: String? = null,
        var origin: String? = null) {

    constructor(rss: Rss) : this() {
        id = rss.id
        title = rss.title
        url = rss.url
        description = rss.description
        origin = rss.origin

        rss.articles.forEach { article -> articles.add(ViewArticle(article)) }
    }

    var articles: MutableList<ViewArticle> = ArrayList()
}
