package ru.iandreyshev.parserrss.ui.adapter

import android.graphics.Bitmap

interface IItemIcon {
    val id: Long

    var isLoaded: Boolean

    fun updateImage(bitmap: Bitmap)
}
