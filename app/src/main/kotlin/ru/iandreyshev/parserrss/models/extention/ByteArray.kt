package ru.iandreyshev.parserrss.models.extention

import android.graphics.Bitmap
import android.graphics.BitmapFactory

val ByteArray.bitmap: Bitmap
    get() = BitmapFactory.decodeByteArray(this, 0, this.size)
