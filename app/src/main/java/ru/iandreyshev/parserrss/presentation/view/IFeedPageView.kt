package ru.iandreyshev.parserrss.presentation.view

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

import ru.iandreyshev.parserrss.models.rss.ViewArticle

interface IFeedPageView : IBaseView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun startUpdate(isStart: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setArticles(newArticles: List<ViewArticle>)

    @StateStrategyType(SkipStrategy::class)
    fun updateImages(isWithoutQueue: Boolean)
}
