package ru.iandreyshev.parserrss.ui.animation

import android.graphics.Bitmap
import android.widget.ImageView

interface IImageAnimation {
    fun start(view: ImageView, imageBitmap: Bitmap)
}
