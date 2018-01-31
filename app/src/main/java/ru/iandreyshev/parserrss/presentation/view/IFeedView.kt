package ru.iandreyshev.parserrss.presentation.view

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

import ru.iandreyshev.parserrss.models.rss.ViewRss

interface IFeedView : IBaseView {
    @StateStrategyType(AddToEndStrategy::class)
    fun insertRss(rss: ViewRss)

    @StateStrategyType(AddToEndStrategy::class)
    fun removeRss(rss: ViewRss)

    @StateStrategyType(SkipStrategy::class)
    fun openArticle(articleId: Long)

    @StateStrategyType(SkipStrategy::class)
    fun openAddingRssDialog()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun startProgressBar(isStart: Boolean)

    @StateStrategyType(SkipStrategy::class)
    fun openRssInfo(rss: ViewRss)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun openEmptyContentMessage(isOpen: Boolean)

    @StateStrategyType(SkipStrategy::class)
    fun openPage(position: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setToolbarScrollable(isScrollable: Boolean)
}
