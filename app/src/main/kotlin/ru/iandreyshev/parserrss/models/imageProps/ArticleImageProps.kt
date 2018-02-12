package ru.iandreyshev.parserrss.models.imageProps

import ru.iandreyshev.parserrss.models.extention.bitmap
import ru.iandreyshev.parserrss.models.extention.bytes
import ru.iandreyshev.parserrss.models.extention.scaleToSize

class ArticleImageProps : IImageProperties {
    override fun configureToMemory(originImage: ByteArray): ByteArray {
        return originImage.bitmap
                .scaleToSize(IImageProperties.MAX_IMAGE_SIZE)
                .bytes
    }

    override fun configureToView(originImage: ByteArray): ByteArray {
        return originImage.bitmap
                .scaleToSize(IImageProperties.MAX_IMAGE_SIZE)
                .bytes
    }
}
