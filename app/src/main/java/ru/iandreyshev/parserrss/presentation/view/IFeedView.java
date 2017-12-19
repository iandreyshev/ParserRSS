package ru.iandreyshev.parserrss.presentation.view;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.iandreyshev.parserrss.models.rss.IRssArticle;
import ru.iandreyshev.parserrss.models.rss.IRssFeed;
import ru.iandreyshev.parserrss.models.rss.Rss;

public interface IFeedView extends IBaseView {
    @StateStrategyType(AddToEndStrategy.class)
    void insertFeed(final Rss rss);

    @StateStrategyType(AddToEndStrategy.class)
    void updateFeedList(final Rss rss);

    @StateStrategyType(AddToEndStrategy.class)
    void removeFeed(IRssFeed feed);

    @StateStrategyType(SkipStrategy.class)
    void openArticle(IRssArticle article);

    @StateStrategyType(SkipStrategy.class)
    void openAddingFeedDialog();

    @StateStrategyType(AddToEndStrategy.class)
    void startProgressBar(boolean isStart);

    @StateStrategyType(AddToEndStrategy.class)
    void startRefresh(IRssFeed feed, boolean isStart);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void openInfo(IRssFeed feed);
}
