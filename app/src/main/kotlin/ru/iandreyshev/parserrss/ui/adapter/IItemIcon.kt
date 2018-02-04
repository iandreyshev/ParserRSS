package ru.iandreyshev.parserrss.ui.adapter

import android.graphics.Bitmap

interface IItemIcon {
    val id: Long

    val isLoaded: Boolean

    fun updateImage(bitmap: Bitmap)
}
