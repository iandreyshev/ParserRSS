package ru.iandreyshev.parserrss.models.repository

import io.objectbox.annotation.*
import java.util.ArrayList
import kotlin.jvm.Transient

@Entity
internal data class RssEntity(
        @Id var id: Long = 0,
        var title: String = "",
        @Index var url: String = "",
        var originUrl: String? = null,
        var description: String? = null,
        @Transient var articles: MutableList<ArticleEntity> = ArrayList()) {

    override fun equals(other: Any?): Boolean {
        return other is RssEntity && url == other.url
    }

    override fun hashCode(): Int {
        return url.hashCode()
    }
}
