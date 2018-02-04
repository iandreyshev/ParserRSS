package ru.iandreyshev.parserrss.models.imageProps

import android.graphics.Bitmap

interface IImageProps {

    companion object {
        const val MAX_MEMORY_SIZE = 360
    }

    val maxBytesCount: Long

    fun configureToMemory(originImage: Bitmap): Bitmap

    fun configureToView(originImage: Bitmap): Bitmap
}
