package ru.iandreyshev.parserrss.presentation.view

import android.support.v7.app.AppCompatDialogFragment
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

import ru.iandreyshev.parserrss.models.rss.Rss

interface IFeedView : IBaseView {
    @StateStrategyType(AddToEndStrategy::class)
    fun insertRss(rss: Rss)

    @StateStrategyType(AddToEndStrategy::class)
    fun removeRss(rssId: Long)

    @StateStrategyType(SkipStrategy::class)
    fun openRssPage(rssId: Long)

    @StateStrategyType(SkipStrategy::class)
    fun openArticle(articleId: Long)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun startProgressBar(isStart: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun openEmptyContentMessage(isOpen: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setToolbarScrollable(isScrollable: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun enableAddButton(isEnabled: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun enableInfoButton(isEnabled: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun enableDeleteButton(isEnabled: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun openToolbarTitle(isOpen: Boolean)

    @StateStrategyType(SkipStrategy::class)
    fun openDialog(newDialog: AppCompatDialogFragment, isSingleOnly: Boolean = true)

    @StateStrategyType(SkipStrategy::class)
    fun closeDialog()
}
