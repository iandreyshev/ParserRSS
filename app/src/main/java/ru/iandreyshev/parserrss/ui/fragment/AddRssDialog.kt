package ru.iandreyshev.parserrss.ui.fragment

import android.app.Dialog
import android.support.v4.app.FragmentManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater

import com.arellomobile.mvp.MvpAppCompatDialogFragment

import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.ui.listeners.IOnSubmitAddRssListener

import kotlinx.android.synthetic.main.dialog_add_feed.*
import java.lang.ref.WeakReference

class AddRssDialog : MvpAppCompatDialogFragment() {

    companion object {
        private val TAG = AddRssDialog::class.java.name
        fun show(fragmentManager: FragmentManager) = AddRssDialog().show(fragmentManager, TAG)
    }

    private var submitListener: WeakReference<IOnSubmitAddRssListener>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_add_feed, null)

        return AlertDialog.Builder(view.context)
                .setTitle(R.string.add_feed_dialog_title)
                .setView(view)
                .setPositiveButton(R.string.add_feed_dialog_submit_button, { _, _ -> submitListener?.get()?.addRss(dialog.urlField.text.toString()) })
                .setNegativeButton(R.string.add_feed_dialog_cancel_button, null)
                .create()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        submitListener = WeakReference(context as IOnSubmitAddRssListener)
    }
}
