package ru.iandreyshev.parserrss.models.imageProps;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

public interface IImageProps {
    @NonNull
    Bitmap configure(@NonNull final Bitmap originImage);
}
