package ru.iandreyshev.parserrss.ui.fragment

import android.widget.Toast

import com.arellomobile.mvp.MvpAppCompatFragment

import ru.iandreyshev.parserrss.presentation.view.IBaseView

abstract class BaseFragment : MvpAppCompatFragment(), IBaseView {
    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
