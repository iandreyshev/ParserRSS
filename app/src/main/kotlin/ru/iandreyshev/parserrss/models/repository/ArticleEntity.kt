package ru.iandreyshev.parserrss.models.repository

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index

@Entity
internal data class ArticleEntity(
        @Id var id: Long = 0,
        @Index var rssId: Long = 0,
        var title: String? = null,
        var description: String? = null,
        var originUrl: String? = null,
        var imageUrl: String? = null,
        var date: Long? = null) {

    override fun equals(other: Any?): Boolean {
        return other is ArticleEntity && originUrl == other.originUrl
    }

    override fun hashCode(): Int {
        return originUrl?.hashCode() ?: 0
    }
}
