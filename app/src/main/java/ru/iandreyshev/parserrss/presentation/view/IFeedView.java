package ru.iandreyshev.parserrss.presentation.view;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.rss.RssArticle;
import ru.iandreyshev.parserrss.models.rss.RssFeed;

public interface IFeedView extends IBaseView {
    @StateStrategyType(AddToEndStrategy.class)
    void insertRss(final Rss rss);

    @StateStrategyType(AddToEndStrategy.class)
    void updateArticles(final Rss rss);

    @StateStrategyType(AddToEndStrategy.class)
    void removeRss(final RssFeed feed);

    @StateStrategyType(SkipStrategy.class)
    void openArticle(final RssArticle article);

    @StateStrategyType(SkipStrategy.class)
    void openAddingRssDialog();

    @StateStrategyType(AddToEndStrategy.class)
    void startProgressBar(boolean isStart);

    @StateStrategyType(AddToEndStrategy.class)
    void startRefresh(final RssFeed feed, boolean isStart);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void openRssInfo(final RssFeed feed);
}
