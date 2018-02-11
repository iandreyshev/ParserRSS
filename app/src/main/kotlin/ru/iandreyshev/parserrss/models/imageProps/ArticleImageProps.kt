package ru.iandreyshev.parserrss.models.imageProps

import android.graphics.Bitmap
import ru.iandreyshev.parserrss.models.extention.scaleToSize

class ArticleImageProps : IImageProperties {
    override fun configureToMemory(originImage: Bitmap): Bitmap {
        return originImage.scaleToSize(IImageProperties.MAX_IMAGE_SIZE)
    }

    override fun configureToView(originImage: Bitmap): Bitmap {
        return originImage.scaleToSize(IImageProperties.MAX_IMAGE_SIZE)
    }
}
