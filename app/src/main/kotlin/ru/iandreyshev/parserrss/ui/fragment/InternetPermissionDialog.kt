package ru.iandreyshev.parserrss.ui.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import com.arellomobile.mvp.MvpAppCompatDialogFragment
import kotlinx.android.synthetic.main.dialog_rss_info.view.*
import ru.iandreyshev.parserrss.R
import android.content.Intent
import android.net.Uri
import android.provider.Settings

class InternetPermissionDialog : MvpAppCompatDialogFragment() {

    companion object {
        private const val PACKAGE_NAME_KEY = "package"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_rss_info, null)
        view.titleView.text = getString(R.string.internet_permission_denied_dialog_title)
        view.descriptionView.text = getString(R.string.internet_permission_denied_dialog_description)

        return AlertDialog.Builder(view.context)
                .setView(view)
                .setPositiveButton(R.string.internet_permission_denied_dialog_accept, null)
                .setNeutralButton(R.string.internet_permission_denied_dialog_open_settings, { _, _ -> openSettings() })
                .create()
    }

    private fun openSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts(PACKAGE_NAME_KEY, activity?.packageName, null)
        startActivity(intent)
    }
}
