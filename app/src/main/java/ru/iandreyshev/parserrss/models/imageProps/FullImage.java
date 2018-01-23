package ru.iandreyshev.parserrss.models.imageProps;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

public final class FullImage implements IImageProps {
    @NonNull
    public static FullImage newInstance() {
        return new FullImage();
    }

    @NonNull
    @Override
    public Bitmap configure(@NonNull Bitmap originImage) {
        return originImage;
    }
}
