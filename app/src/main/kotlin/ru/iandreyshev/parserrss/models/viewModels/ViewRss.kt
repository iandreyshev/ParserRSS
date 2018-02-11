package ru.iandreyshev.parserrss.models.viewModels

import ru.iandreyshev.parserrss.models.repository.Rss

data class ViewRss(
        var id: Long = 0,
        var title: String? = null,
        var description: String? = null,
        var originUrl: String? = null) {

    constructor(rss: Rss) : this() {
        id = rss.id
        title = rss.title
        description = rss.description
        originUrl = rss.originUrl
    }
}
