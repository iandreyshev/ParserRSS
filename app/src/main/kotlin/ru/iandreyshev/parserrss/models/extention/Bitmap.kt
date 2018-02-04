package ru.iandreyshev.parserrss.models.extention

import android.graphics.Bitmap

fun Bitmap.scaleToSize(maxSize: Int): Bitmap {
    if (maxSize <= 0) {
        return this
    } else if (width <= maxSize && height <= maxSize) {
        return this
    }

    val currMaxSize = Math.max(width, height)
    val sizeFactor = maxSize.toFloat() / currMaxSize.toFloat()

    val newWidth = Math.max((width * sizeFactor).toInt(), 1)
    val newHeight = Math.max((height * sizeFactor).toInt(), 1)

    return Bitmap.createScaledBitmap(this, newWidth, newHeight, true)
}
