package ru.iandreyshev.parserrss.models.repository.extention

import ru.iandreyshev.parserrss.models.repository.ArticleEntity
import ru.iandreyshev.parserrss.models.rss.Article

internal val ArticleEntity.domainModel
    get() = Article(
            id = this.id,
            rssId = this.rssId,
            title = this.title,
            description = this.description,
            date = this.date,
            originUrl = this.originUrl,
            imageUrl = this.imageUrl
    )

internal val Article.entity
    get() = ArticleEntity(
            id = this.id,
            title = this.title,
            description = this.description,
            date = this.date,
            originUrl = this.originUrl,
            imageUrl = this.imageUrl
    )
