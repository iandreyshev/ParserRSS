package ru.iandreyshev.parserrss.presentation.view.settings;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface SettingsView extends MvpView {
    void openFeed();

    @StateStrategyType(SkipStrategy.class)
    void openAddingForm();
}
