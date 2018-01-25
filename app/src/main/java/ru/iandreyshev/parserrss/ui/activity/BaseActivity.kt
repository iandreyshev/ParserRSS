package ru.iandreyshev.parserrss.ui.activity

import android.content.Intent
import android.net.Uri
import com.arellomobile.mvp.MvpAppCompatActivity
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

import ru.iandreyshev.parserrss.presentation.view.IBaseView

abstract class BaseActivity : MvpAppCompatActivity(), IBaseView {
    override fun showShortToast(message: String) {
        toast(message)
    }

    override fun showLongToast(message: String) {
        longToast(message)
    }

    override fun openInBrowser(url: Uri) {
        startActivity(Intent(Intent.ACTION_VIEW, url))
    }
}
