package ru.iandreyshev.parserrss.models.repository.extention

import ru.iandreyshev.parserrss.models.repository.RssEntity
import ru.iandreyshev.parserrss.models.rss.Rss

internal val RssEntity.domainModel: Rss
    get() = Rss(
            id = this.id,
            title = this.title,
            description = this.description,
            url = this.url,
            originUrl = this.originUrl,
            articles = ArrayList(this.articles.map { it.domainModel })
    )

internal val Rss.entity: RssEntity
    get() = RssEntity(
            id = this.id,
            title = this.title ?: "",
            description = this.description,
            originUrl = this.originUrl,
            url = this.url ,
            articles = ArrayList(this.articles.map { it.entity })
    )
