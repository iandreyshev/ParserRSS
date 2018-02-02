package ru.iandreyshev.parserrss.ui.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import com.arellomobile.mvp.MvpAppCompatDialogFragment
import kotlinx.android.synthetic.main.dialog_rss_info.view.*
import ru.iandreyshev.parserrss.R
import java.lang.ref.WeakReference

class InternetPermissionDialog : MvpAppCompatDialogFragment() {

    companion object {
        private val TAG = InternetPermissionDialog::class.java.name
        fun show(fragmentManager: FragmentManager) = InternetPermissionDialog().show(fragmentManager, TAG)
    }

    interface IOnOpenSettingsListener {
        fun openSettings()
    }

    private var openSettingsListener: WeakReference<IOnOpenSettingsListener>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_rss_info, null)
        view.titleView.text = getString(R.string.internet_permission_denied_dialog_title)
        view.descriptionView.text = getString(R.string.internet_permission_denied_dialog_description)

        return AlertDialog.Builder(view.context)
                .setView(view)
                .setPositiveButton(
                        R.string.internet_permission_denied_dialog_accept,
                        { _, _ -> openSettingsListener?.get()?.openSettings() })
                .setNeutralButton(R.string.internet_permission_denied_dialog_open_settings, null)
                .create()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        openSettingsListener = WeakReference(context as IOnOpenSettingsListener)
    }
}
