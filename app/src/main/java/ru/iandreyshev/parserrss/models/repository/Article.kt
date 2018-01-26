package ru.iandreyshev.parserrss.models.repository

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Article (
        @Id var id: Long = 0,
        var rssId: Long = 0,
        var title: String = "",
        var description: String = "",
        var originUrl: String = "",
        var imageUrl: String? = null,
        var date: Long? = null) {

    override fun equals(other: Any?): Boolean {
        return other is Article && originUrl == other.originUrl
    }

    override fun hashCode(): Int {
        return originUrl.hashCode()
    }
}
