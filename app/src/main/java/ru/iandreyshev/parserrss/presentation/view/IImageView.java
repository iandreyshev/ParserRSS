package ru.iandreyshev.parserrss.presentation.view;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface IImageView extends MvpView {
    @StateStrategyType(SkipStrategy.class)
    void insertImage(@NonNull Bitmap bitmap);
}
