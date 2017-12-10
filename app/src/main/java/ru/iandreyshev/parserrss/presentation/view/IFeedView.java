package ru.iandreyshev.parserrss.presentation.view;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.iandreyshev.parserrss.models.article.IArticleInfo;
import ru.iandreyshev.parserrss.models.feed.IFeedInfo;
import ru.iandreyshev.parserrss.presentation.view.IBaseView;

public interface IFeedView extends IBaseView {
    @StateStrategyType(SkipStrategy.class)
    void openSettings();

    @StateStrategyType(SkipStrategy.class)
    void openArticle(IArticleInfo article);

    @StateStrategyType(AddToEndStrategy.class)
    void updateFeedList(IFeedInfo feed, List<IArticleInfo> newList);

    @StateStrategyType(SkipStrategy.class)
    void setRefreshing(boolean isRefresh);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void openAddingFeedDialog();

    @StateStrategyType(SkipStrategy.class)
    void startProgressBar(boolean isStart);
}
