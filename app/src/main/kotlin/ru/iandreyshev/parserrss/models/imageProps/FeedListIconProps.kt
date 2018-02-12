package ru.iandreyshev.parserrss.models.imageProps

import ru.iandreyshev.parserrss.models.extention.bitmap
import ru.iandreyshev.parserrss.models.extention.bytes
import ru.iandreyshev.parserrss.models.extention.scaleToSize

class FeedListIconProps : IImageProperties {
    companion object {
        private const val MAX_SIZE = 144
    }

    override fun configureToView(originImage: ByteArray): ByteArray {
        return originImage.bitmap
                .scaleToSize(MAX_SIZE)
                .bytes
    }

    override fun configureToMemory(originImage: ByteArray): ByteArray {
        return originImage.bitmap
                .scaleToSize(IImageProperties.MAX_IMAGE_SIZE)
                .bytes
    }
}
