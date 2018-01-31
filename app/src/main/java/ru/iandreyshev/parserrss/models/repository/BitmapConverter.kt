package ru.iandreyshev.parserrss.models.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log

import java.io.ByteArrayOutputStream

import io.objectbox.converter.PropertyConverter

internal class BitmapConverter : PropertyConverter<Bitmap, ByteArray> {
    companion object {
        private val TAG = BitmapConverter::class.java.name
        private const val QUALITY = 100
        private val COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG
    }

    override fun convertToEntityProperty(databaseValue: ByteArray?): Bitmap? {
        return when (databaseValue) {
            null -> null
            else -> BitmapFactory.decodeByteArray(databaseValue, 0, databaseValue.size)
        }
    }

    override fun convertToDatabaseValue(entityProperty: Bitmap?): ByteArray? {
        entityProperty ?: return null

        try {
            ByteArrayOutputStream().use { stream ->
                entityProperty.compress(COMPRESS_FORMAT, QUALITY, stream)

                return stream.toByteArray()
            }
        } catch (ex: Exception) {
            Log.e(TAG, Log.getStackTraceString(ex))
        }

        return null
    }
}
