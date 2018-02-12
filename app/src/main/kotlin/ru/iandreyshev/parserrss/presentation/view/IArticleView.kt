package ru.iandreyshev.parserrss.presentation.view

import android.graphics.Bitmap
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

import ru.iandreyshev.parserrss.models.rss.Article
import ru.iandreyshev.parserrss.models.rss.Rss

interface IArticleView : IBaseView {
    @StateStrategyType(SkipStrategy::class)
    fun closeArticle()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun initArticle(rss: Rss, article: Article)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setImage(imageBitmap: Bitmap)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun startProgressBar(isStart: Boolean)
}
