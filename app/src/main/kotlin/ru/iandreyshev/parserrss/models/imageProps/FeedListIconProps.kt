package ru.iandreyshev.parserrss.models.imageProps

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import ru.iandreyshev.parserrss.models.extention.scaleToSize
import java.io.ByteArrayOutputStream

class FeedListIconProps {
    companion object : IImageProps {
        private val TAG = FeedListIconProps::class.java.name
        private val FORMAT = Bitmap.CompressFormat.JPEG
        private const val QUALITY = 25
        private const val MAX_SIZE = 144

        val newInstance = FeedListIconProps()

        override fun configureToView(originImage: Bitmap): Bitmap {
            val copy = originImage.scaleToSize(MAX_SIZE)

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

        override fun configureToMemory(originImage: Bitmap): Bitmap {
            return originImage.scaleToSize(IImageProps.MAX_MEMORY_SIZE)
        }
    }
}
