package ru.iandreyshev.parserrss.models.rss

import ru.iandreyshev.parserrss.models.repository.Article

class ViewArticle(article: Article) {
    val id: Long = article.id
    val title: String = article.title
    val originUrl: String = article.originUrl
    val description: String = article.description
    val date: Long? = article.date

    override fun equals(other: Any?): Boolean {
        return other is ViewArticle && originUrl == other.originUrl
    }

    override fun hashCode(): Int {
        return originUrl.hashCode()
    }
}
