package ru.iandreyshev.parserrss.presentation.view.feed;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.iandreyshev.parserrss.models.article.IArticleInfo;
import ru.iandreyshev.parserrss.models.feed.IFeedInfo;
import ru.iandreyshev.parserrss.presentation.view.BaseView;

public interface FeedView extends BaseView {
    @StateStrategyType(SkipStrategy.class)
    void openSettings();

    @StateStrategyType(SkipStrategy.class)
    void openArticle();

    @StateStrategyType(AddToEndStrategy.class)
    void addArticle(IArticleInfo article);

    @StateStrategyType(AddToEndStrategy.class)
    void addFeed(IFeedInfo feed);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void clearFeed();

    @StateStrategyType(SkipStrategy.class)
    void setRefreshing(boolean isRefresh);
}
