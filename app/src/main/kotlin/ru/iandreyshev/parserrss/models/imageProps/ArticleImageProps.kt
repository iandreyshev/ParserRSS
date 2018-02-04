package ru.iandreyshev.parserrss.models.imageProps

import android.graphics.Bitmap
import ru.iandreyshev.parserrss.models.extention.scaleToSize

class ArticleImageProps {
    companion object : IImageProps {
        private const val MAX_BYTES_COUNT = 1048576L // 1MB

        override val maxBytesCount: Long
            get() = MAX_BYTES_COUNT

        override fun configureToMemory(originImage: Bitmap): Bitmap {
            return originImage.scaleToSize(IImageProps.MAX_MEMORY_SIZE)
        }

        override fun configureToView(originImage: Bitmap): Bitmap {
            return originImage.scaleToSize(IImageProps.MAX_MEMORY_SIZE)
        }
    }
}
