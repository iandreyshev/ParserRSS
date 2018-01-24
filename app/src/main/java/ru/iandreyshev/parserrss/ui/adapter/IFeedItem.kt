package ru.iandreyshev.parserrss.ui.adapter

import android.graphics.Bitmap

interface IFeedItem {
    val isImageLoaded: Boolean

    val id: Long

    fun updateImage(bitmap: Bitmap)
}
