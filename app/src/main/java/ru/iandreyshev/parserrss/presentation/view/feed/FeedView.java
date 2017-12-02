package ru.iandreyshev.parserrss.presentation.view.feed;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.iandreyshev.parserrss.models.feed.Article;

public interface FeedView extends MvpView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void setFeedName(final String name);

    @StateStrategyType(SkipStrategy.class)
    void openSettings();

    @StateStrategyType(SkipStrategy.class)
    void openArticle();

    void addItem(Article item);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void clearItems();
}
