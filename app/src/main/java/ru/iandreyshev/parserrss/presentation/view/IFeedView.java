package ru.iandreyshev.parserrss.presentation.view;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.models.rss.IViewArticle;

public interface IFeedView extends IBaseView {
    @StateStrategyType(AddToEndStrategy.class)
    void insertRss(final IViewRss rss);

    @StateStrategyType(AddToEndStrategy.class)
    void removeRss(final IViewRss rss);

    @StateStrategyType(SkipStrategy.class)
    void openArticle(long articleId);

    @StateStrategyType(SkipStrategy.class)
    void openAddingRssDialog();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void startProgressBar(boolean isStart);

    @StateStrategyType(SkipStrategy.class)
    void enableAddingButton(boolean isEnable);

    @StateStrategyType(SkipStrategy.class)
    void enableDeleteButton(boolean isEnable);

    @StateStrategyType(SkipStrategy.class)
    void enableInfoButton(boolean isEnable);

    @StateStrategyType(SkipStrategy.class)
    void openRssInfo(final IViewRss rss);

    @StateStrategyType(SkipStrategy.class)
    void onFeedUpdate();
}
