package ru.iandreyshev.parserrss.presentation.view;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.iandreyshev.parserrss.models.rss.IViewArticle;

public interface IFeedTabView extends IBaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void startUpdate(boolean isStart);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setArticles(final List<IViewArticle> newArticles);
}
