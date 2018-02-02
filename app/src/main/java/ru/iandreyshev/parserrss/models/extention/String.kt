package ru.iandreyshev.parserrss.models.extention

import android.net.Uri

val String.uri: Uri?
    get() {
        return  try {
            Uri.parse(this)
        } catch (ex: Exception) {
            null
        }
    }
