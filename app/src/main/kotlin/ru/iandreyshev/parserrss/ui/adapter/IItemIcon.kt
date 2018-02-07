package ru.iandreyshev.parserrss.ui.adapter

import android.graphics.Bitmap

interface IItemIcon {
    val id: Long

    val isUpdateStart: Boolean

    fun onStartUpdate()

    fun updateImage(bitmap: Bitmap)
}
