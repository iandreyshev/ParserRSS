package ru.iandreyshev.parserrss.presentation.view;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.iandreyshev.parserrss.models.rss.ViewArticle;

public interface IFeedPageView extends IBaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void startUpdate(boolean isStart);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setArticles(final List<ViewArticle> newArticles);

    @StateStrategyType(SkipStrategy.class)
    void updateImages(boolean isWithoutQueue);
}
