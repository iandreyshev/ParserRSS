package ru.iandreyshev.parserrss.models.imageProps

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log

import java.io.ByteArrayOutputStream

class FeedListIconProps : IImageProps {
    companion object {
        private val TAG = FeedListIconProps::class.java.name
        private val FORMAT = Bitmap.CompressFormat.JPEG
        private const val QUALITY = 25
        private const val SIZE_FACTOR = 0.4f

        val newInstance = FeedListIconProps()
    }

    override fun configure(originImage: Bitmap): Bitmap {
        val newWidth = (originImage.width * SIZE_FACTOR).toInt()
        val newHeight = (originImage.height * SIZE_FACTOR).toInt()
        val copy = Bitmap.createScaledBitmap(originImage, newWidth, newHeight, false)

        try {
            ByteArrayOutputStream().use { stream ->
                copy.compress(FORMAT, QUALITY, stream)
                val bytes = stream.toByteArray()

                return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            }
        } catch (ex: Exception) {
            Log.e(TAG, Log.getStackTraceString(ex))
        }

        return originImage
    }
}
