package ru.iandreyshev.parserrss.models.repository

import android.graphics.Bitmap
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class ArticleImage (
        @Id var id: Long = 0,
        var articleId: Long = 0,
        @Convert(dbType = ByteArray::class, converter = BitmapConverter::class)
        var bitmap: Bitmap? = null)