package ru.iandreyshev.parserrss.presentation.view

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface IRssInfoView : IBaseView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setInfo(title: String?, description: String?)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setOpenOriginalEnabled(isEnabled: Boolean)

    @StateStrategyType(SkipStrategy::class)
    fun close()
}
