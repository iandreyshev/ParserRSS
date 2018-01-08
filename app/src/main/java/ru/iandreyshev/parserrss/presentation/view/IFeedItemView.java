package ru.iandreyshev.parserrss.presentation.view;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface IFeedItemView extends IBaseView {
    @StateStrategyType(SkipStrategy.class)
    void insertImage(@NonNull Bitmap bitmap);
}
