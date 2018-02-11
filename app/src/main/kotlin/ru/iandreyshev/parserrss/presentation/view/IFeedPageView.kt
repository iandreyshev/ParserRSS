package ru.iandreyshev.parserrss.presentation.view

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

import ru.iandreyshev.parserrss.models.viewModels.ViewArticle

interface IFeedPageView : IBaseView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun startUpdate(isStart: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setArticles(newArticles: ArrayList<ViewArticle>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateImages()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun openEmptyContentMessage(isOpen: Boolean)

    @StateStrategyType(SkipStrategy::class)
    fun openInternetPermissionDialog()
}
