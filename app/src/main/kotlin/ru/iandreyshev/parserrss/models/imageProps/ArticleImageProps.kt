package ru.iandreyshev.parserrss.models.imageProps

import android.graphics.Bitmap
import ru.iandreyshev.parserrss.models.extention.scaleToSize

class ArticleImageProps {
    companion object : IImageProps {
        override fun configureToMemory(originImage: Bitmap): Bitmap {
            return originImage.scaleToSize(IImageProps.MAX_IMAGE_SIZE)
        }

        override fun configureToView(originImage: Bitmap): Bitmap {
            return originImage.scaleToSize(IImageProps.MAX_IMAGE_SIZE)
        }
    }
}
