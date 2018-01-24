package ru.iandreyshev.parserrss.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.SimpleDateFormat;
import java.util.Locale;

class Utils {
    companion object {
        private val DATE_FORMAT: SimpleDateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH)
        private const val CUT_TAB_TITLE_PATTERN = "%s..."
        private const val MAX_TAB_TITLE_LINE_LENGTH = 16

        @JvmStatic
        fun truncateTabsTitle(title: String): String {
            var result = title.trim()
            val firstSpacePos = title.indexOf(" ")
            var maxLength = MAX_TAB_TITLE_LINE_LENGTH

            if (firstSpacePos in 1..MAX_TAB_TITLE_LINE_LENGTH) {
                result = title.replaceFirst(" ", "\n")
                maxLength += firstSpacePos
            }

            if (result.length > maxLength) {
                result = title.substring(0, maxLength - 1)
                result = String.format(CUT_TAB_TITLE_PATTERN, result)
            }

            return result;
        }

        @JvmStatic
        fun toBitmap(byteArray: ByteArray?): Bitmap? {
            if (byteArray?.size == null) {
                return null
            }

            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }

        @JvmStatic
        fun toDateString(date: Long?): String = if (date == null) "" else DATE_FORMAT.format(date)
    }
}