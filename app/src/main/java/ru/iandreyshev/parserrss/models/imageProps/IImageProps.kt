package ru.iandreyshev.parserrss.models.imageProps

import android.graphics.Bitmap

interface IImageProps {
    fun configure(originImage: Bitmap): Bitmap
}
