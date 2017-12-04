package ru.iandreyshev.parserrss.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface BaseView extends MvpView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showShortToast(String message);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showLongToast(String message);
}
