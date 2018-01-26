package ru.iandreyshev.parserrss.models.repository

import java.util.ArrayList

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index
import io.objectbox.annotation.Transient

@Entity
data class Rss (
        @Id var id: Long = 0,
        var title: String = "",
        @Index var url: String = "",
        var origin: String = "",
        var description: String? = null) {

    @Transient
    var articles: List<Article> = ArrayList()
        set(newArticles) {
            field = ArrayList(newArticles)
        }

    override fun equals(other: Any?): Boolean {
        return other is Rss && url == other.url
    }

    override fun hashCode(): Int {
        return url.hashCode()
    }
}
