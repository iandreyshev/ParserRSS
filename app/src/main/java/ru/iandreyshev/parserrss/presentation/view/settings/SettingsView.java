package ru.iandreyshev.parserrss.presentation.view.settings;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.iandreyshev.parserrss.models.feed.IFeedInfo;

public interface SettingsView extends MvpView {
    @StateStrategyType(SkipStrategy.class)
    void openFeed();
}
