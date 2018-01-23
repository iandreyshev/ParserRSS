package ru.iandreyshev.parserrss.models.repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;

import io.objectbox.converter.PropertyConverter;

final class BitmapConverter implements PropertyConverter<Bitmap, byte[]> {
    private static final String TAG = BitmapConverter.class.getName();
    private static final int QUALITY = 100;
    private static final Bitmap.CompressFormat COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;

    @Override
    public Bitmap convertToEntityProperty(byte[] databaseValue) {
        if (databaseValue == null) {
            return null;
        }

        return BitmapFactory.decodeByteArray(databaseValue, 0, databaseValue.length);
    }

    @Override
    public byte[] convertToDatabaseValue(Bitmap entityProperty) {
        if (entityProperty == null) {
            return null;
        }

        try (final ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            entityProperty.compress(COMPRESS_FORMAT, QUALITY, stream);

            return stream.toByteArray();

        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
        }

        return null;
    }
}
