package ru.iandreyshev.parserrss.models.rss

class ArticleImage (
        var id: Long = 0,
        var articleId: Long = 0,
        var bytes: ByteArray = byteArrayOf())
