package ru.iandreyshev.parserrss.models.repository

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index

@Entity
internal data class ArticleImageEntity(
        @Id var id: Long = 0,
        @Index var articleId: Long = 0,
        var bytes: ByteArray = byteArrayOf())
