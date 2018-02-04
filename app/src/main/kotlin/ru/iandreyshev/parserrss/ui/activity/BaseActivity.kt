package ru.iandreyshev.parserrss.ui.activity

import com.arellomobile.mvp.MvpAppCompatActivity
import org.jetbrains.anko.toast

import ru.iandreyshev.parserrss.presentation.view.IBaseView

abstract class BaseActivity : MvpAppCompatActivity(), IBaseView {
    override fun showToast(message: String) {
        toast(message)
    }
}
