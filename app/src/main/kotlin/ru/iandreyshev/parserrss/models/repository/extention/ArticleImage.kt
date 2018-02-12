package ru.iandreyshev.parserrss.models.repository.extention

import ru.iandreyshev.parserrss.models.repository.ArticleImageEntity
import ru.iandreyshev.parserrss.models.rss.ArticleImage

internal val ArticleImage.entity: ArticleImageEntity
    get() = ArticleImageEntity(
            id = this.id,
            articleId = this.articleId,
            bytes = this.bytes
    )

internal val ArticleImageEntity.domainModel: ArticleImage
    get() = ArticleImage(
            id = this.id,
            articleId = this.articleId,
            bytes = this.bytes
    )
