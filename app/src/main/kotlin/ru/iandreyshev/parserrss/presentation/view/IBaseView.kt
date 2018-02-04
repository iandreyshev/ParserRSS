package ru.iandreyshev.parserrss.presentation.view

import android.content.Intent
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface IBaseView : MvpView {
    @StateStrategyType(SkipStrategy::class)
    fun showToast(message: String)

    @StateStrategyType(SkipStrategy::class)
    fun startActivity(intent: Intent)
}
