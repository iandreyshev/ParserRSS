package ru.iandreyshev.parserrss.models.imageProps

import android.graphics.Bitmap

class FullImage : IImageProps {
    companion object {
        val newInstance = FullImage()
    }

    override fun configure(originImage: Bitmap) = originImage
}
