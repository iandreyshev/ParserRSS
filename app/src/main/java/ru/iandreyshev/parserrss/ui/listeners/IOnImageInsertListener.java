package ru.iandreyshev.parserrss.ui.listeners;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

public interface IOnImageInsertListener {
    void insert(@NonNull Bitmap bitmap);
}