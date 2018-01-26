package ru.iandreyshev.parserrss.presentation.view

import android.graphics.Bitmap

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface IImageView : MvpView {
    @StateStrategyType(SkipStrategy::class)
    fun insertImage(bitmap: Bitmap)
}
