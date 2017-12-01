package ru.iandreyshev.parserrss.presentation.view.feed;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface FeedView extends MvpView {
    void setFeedName(final String name);

    @StateStrategyType(SkipStrategy.class)
    void openSettings();
}
