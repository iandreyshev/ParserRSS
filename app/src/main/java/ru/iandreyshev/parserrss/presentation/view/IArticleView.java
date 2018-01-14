package ru.iandreyshev.parserrss.presentation.view;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.iandreyshev.parserrss.models.rss.IViewArticle;

public interface IArticleView extends IBaseView {
    @StateStrategyType(SkipStrategy.class)
    void closeArticle();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void initArticle(@NonNull final IViewArticle article);
}
