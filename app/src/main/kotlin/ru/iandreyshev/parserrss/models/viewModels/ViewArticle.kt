package ru.iandreyshev.parserrss.models.viewModels

import ru.iandreyshev.parserrss.models.repository.Article

data class ViewArticle(
        var id: Long = 0,
        var title: String? = null,
        var originUrl: String? = null,
        var description: String? = null,
        var date: Long? = null) {

    constructor(article: Article) : this() {
        id = article.id
        title = article.title
        originUrl = article.originUrl
        description = article.description
        date = article.date
    }

    override fun equals(other: Any?): Boolean {
        return other is ViewArticle && originUrl == other.originUrl
    }

    override fun hashCode(): Int {
        return originUrl?.hashCode() ?: 0
    }
}
