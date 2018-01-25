package ru.iandreyshev.parserrss.presentation.view

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.iandreyshev.parserrss.models.rss.ViewRss

interface IRssInfoView : IBaseView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setInfo(rss: ViewRss)
}
