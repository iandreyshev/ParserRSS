package ru.iandreyshev.parserrss.presentation.view.article;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface ArticleView extends MvpView {
    @StateStrategyType(SkipStrategy.class)
    void openFeed();
}
