package ru.iandreyshev.parserrss.models.viewModels

import ru.iandreyshev.parserrss.models.repository.Article

data class ViewArticle(
        var id: Long = 0,
        var title: String? = null,
        var description: String? = null,
        var date: Long? = null,
        var originUrl: String? = null) {

    constructor(article: Article) : this() {
        id = article.id
        title = article.title
        description = article.description
        date = article.date
        originUrl = article.originUrl
    }
}
