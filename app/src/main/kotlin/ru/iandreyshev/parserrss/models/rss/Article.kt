package ru.iandreyshev.parserrss.models.rss

data class Article(
        var id: Long = 0,
        var rssId: Long = 0,
        var title: String? = null,
        var description: String? = null,
        var date: Long? = null,
        var originUrl: String? = null,
        var imageUrl: String? = null)
