package ru.iandreyshev.parserrss.models.imageProps

interface IImageProperties {
    companion object {
        const val MAX_IMAGE_SIZE = 480
    }

    fun configureToMemory(originImage: ByteArray): ByteArray

    fun configureToView(originImage: ByteArray): ByteArray
}
