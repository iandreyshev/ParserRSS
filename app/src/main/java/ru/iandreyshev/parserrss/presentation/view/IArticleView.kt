package ru.iandreyshev.parserrss.presentation.view

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss

interface IArticleView : IBaseView {
    @StateStrategyType(SkipStrategy::class)
    fun closeArticle()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun initArticle(rss: ViewRss, article: ViewArticle)
}
