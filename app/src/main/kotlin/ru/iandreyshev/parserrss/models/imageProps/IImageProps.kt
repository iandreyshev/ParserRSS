package ru.iandreyshev.parserrss.models.imageProps

import android.graphics.Bitmap

interface IImageProps {
    companion object {
        const val MAX_IMAGE_SIZE = 480
    }

    fun configureToMemory(originImage: Bitmap): Bitmap

    fun configureToView(originImage: Bitmap): Bitmap
}
