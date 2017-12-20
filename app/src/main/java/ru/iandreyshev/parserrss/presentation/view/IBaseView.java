package ru.iandreyshev.parserrss.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface IBaseView extends MvpView {
    @StateStrategyType(SkipStrategy.class)
    void showShortToast(final String message);

    @StateStrategyType(SkipStrategy.class)
    void showLongToast(final String message);
}
