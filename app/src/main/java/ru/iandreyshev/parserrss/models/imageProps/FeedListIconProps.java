package ru.iandreyshev.parserrss.models.imageProps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public final class FeedListIconProps implements IImageProps {
    private static final String TAG = FeedListIconProps.class.getName();
    private static final Bitmap.CompressFormat FORMAT = Bitmap.CompressFormat.JPEG;
    private static final int QUALITY = 25;

    @NonNull
    public static FeedListIconProps newInstance() {
        return new FeedListIconProps();
    }

    @NonNull
    @Override
    public Bitmap configure(@NonNull Bitmap originImage) {
        final Bitmap copy = Bitmap.createBitmap(originImage);

        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            copy.compress(FORMAT, QUALITY, stream);
            byte[] bytes = stream.toByteArray();

            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
        }

        return originImage;
    }
}
