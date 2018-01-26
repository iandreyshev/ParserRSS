package ru.iandreyshev.parserrss.ui.extention

import java.text.SimpleDateFormat
import java.util.*

private val DATE_FORMAT: SimpleDateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH)

internal val Long.dateString: String
    get () = DATE_FORMAT.format(this)
