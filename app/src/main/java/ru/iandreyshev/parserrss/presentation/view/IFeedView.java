package ru.iandreyshev.parserrss.presentation.view;

import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.iandreyshev.parserrss.models.article.IArticleContent;
import ru.iandreyshev.parserrss.models.feed.IFeedContent;

public interface IFeedView extends IBaseView {
    @StateStrategyType(SkipStrategy.class)
    void openArticle(IArticleContent article);

    @StateStrategyType(AddToEndStrategy.class)
    void updateFeedList(IFeedContent feed, List<IArticleContent> newList);

    @StateStrategyType(SkipStrategy.class)
    void setRefreshing(boolean isRefresh);

    @StateStrategyType(SkipStrategy.class)
    void openAddingFeedDialog();

    @StateStrategyType(AddToEndStrategy.class)
    void startProgressBar(boolean isStart);

    @StateStrategyType(SkipStrategy.class)
    void setFeed(IFeedContent feed);
}
