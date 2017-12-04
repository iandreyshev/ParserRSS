package ru.iandreyshev.parserrss.presentation.view.article;

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.iandreyshev.parserrss.presentation.view.BaseView;

public interface ArticleView extends BaseView {
    @StateStrategyType(SkipStrategy.class)
    void openFeed();
}
