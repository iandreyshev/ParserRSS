package ru.iandreyshev.parserrss.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater

import com.arellomobile.mvp.MvpAppCompatDialogFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.dialog_rss_info.*
import org.jetbrains.anko.toast

import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.models.viewModels.ViewRss
import ru.iandreyshev.parserrss.presentation.presenter.RssInfoPresenter
import ru.iandreyshev.parserrss.presentation.view.IRssInfoView

class RssInfoDialog : MvpAppCompatDialogFragment(), IRssInfoView {

    companion object {
        private const val OPEN_ORIGINAL_BUTTON = AlertDialog.BUTTON_NEUTRAL

        fun newInstance(rss: ViewRss): RssInfoDialog {
            val dialog = RssInfoDialog()
            dialog.mPresenter = RssInfoPresenter(rss)

            return dialog
        }
    }

    @InjectPresenter
    lateinit var mPresenter: RssInfoPresenter

    private val mInteractor
        get() = mPresenter.interactor

    @ProvidePresenter
    fun provideRssInfoPresenter() = mPresenter

    override fun onCreateDialog(savedInstantState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_rss_info, null)

        return AlertDialog.Builder(view.context)
                .setView(view)
                .setPositiveButton(R.string.rss_info_dialog_ok_button, { _, _ -> })
                .setNeutralButton(R.string.rss_info_open_original_button, { _, _ -> mInteractor.onOpenOriginal() })
                .create()
    }

    override fun loadData(title: String, description: String?) {
        dialog.titleView.text = title
        dialog.descriptionView.text = description ?: getString(R.string.rss_info_dialog_default_description)
    }

    override fun setOpenOriginalEnabled(isEnabled: Boolean) {
        (dialog as AlertDialog).getButton(OPEN_ORIGINAL_BUTTON).isEnabled = isEnabled
    }

    override fun close() = dialog.cancel()

    override fun showToast(message: String) {
        context?.toast(message)
    }
}
