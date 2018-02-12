package ru.iandreyshev.parserrss.models.rss

data class Rss(
        var id: Long = 0,
        var title: String? = null,
        var description: String? = null,
        var url: String = "",
        var originUrl: String? = null,
        var articles: MutableList<Article> = ArrayList())
