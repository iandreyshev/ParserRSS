package ru.iandreyshev.parserrss.presentation.view;

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface IArticleView extends IBaseView {
    @StateStrategyType(SkipStrategy.class)
    void openFeed();
}
