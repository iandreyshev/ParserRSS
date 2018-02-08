package ru.iandreyshev.parserrss.presentation.view

import android.graphics.Bitmap
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

import ru.iandreyshev.parserrss.models.viewModels.ViewArticle
import ru.iandreyshev.parserrss.models.viewModels.ViewRss

interface IArticleView : IBaseView {
    @StateStrategyType(SkipStrategy::class)
    fun closeArticle()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun initArticle(rss: ViewRss, article: ViewArticle)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setImage(imageBitmap: Bitmap)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun startProgressBar(isStart: Boolean)
}
