package ru.iandreyshev.parserrss.ui.adapter

import android.graphics.Bitmap

interface IItemIcon {
    val id: Long

    fun updateImage(bitmap: Bitmap)
}
