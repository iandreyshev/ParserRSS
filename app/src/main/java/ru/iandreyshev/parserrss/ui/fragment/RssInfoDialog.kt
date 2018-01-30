package ru.iandreyshev.parserrss.ui.fragment

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater

import com.arellomobile.mvp.MvpAppCompatDialogFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.dialog_rss_info.*

import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.presentation.presenter.RssInfoPresenter
import ru.iandreyshev.parserrss.presentation.view.IRssInfoView

class RssInfoDialog : MvpAppCompatDialogFragment(), IRssInfoView {
    companion object {
        fun show(fragmentManager: FragmentManager, rss: ViewRss) {
            val dialog = RssInfoDialog()
            dialog.presenter = RssInfoPresenter(rss)
            dialog.show(fragmentManager, RssInfoDialog::class.java.name)
        }
    }

    @InjectPresenter
    lateinit var presenter: RssInfoPresenter

    @ProvidePresenter
    fun provideRssInfoPresenter() = presenter

    override fun onCreateDialog(savedInstantState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_rss_info, null)

        return AlertDialog.Builder(view.context)
                .setView(view)
                .setPositiveButton(R.string.rss_info_dialog_button, null)
                .setNeutralButton(R.string.rss_info_open_original_button) { _, _ -> presenter.onOpenOriginal() }
                .create()
    }

    override fun setInfo(rss: ViewRss) {
        dialog.rssTitle?.text = rss.title
        dialog.rssDescription?.text = rss.description
    }

    override fun openInBrowser(url: Uri) {
        startActivity(Intent(Intent.ACTION_VIEW, url))
    }

    override fun showShortToast(message: String) {}

    override fun showLongToast(message: String) {}
}
