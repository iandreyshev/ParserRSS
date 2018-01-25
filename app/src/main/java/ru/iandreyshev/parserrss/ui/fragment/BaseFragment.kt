package ru.iandreyshev.parserrss.ui.fragment

import android.content.Intent
import android.net.Uri
import android.widget.Toast

import com.arellomobile.mvp.MvpAppCompatFragment

import ru.iandreyshev.parserrss.presentation.view.IBaseView

abstract class BaseFragment : MvpAppCompatFragment(), IBaseView {
    override fun showShortToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLongToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun openInBrowser(url: Uri) {
        startActivity(Intent(Intent.ACTION_VIEW, url))
    }
}
