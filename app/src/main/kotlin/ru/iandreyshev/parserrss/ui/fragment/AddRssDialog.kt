package ru.iandreyshev.parserrss.ui.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.InputFilter
import android.view.LayoutInflater
import android.widget.TextView

import com.arellomobile.mvp.MvpAppCompatDialogFragment

import ru.iandreyshev.parserrss.R

import kotlinx.android.synthetic.main.dialog_add_feed.*
import kotlinx.android.synthetic.main.dialog_add_feed.view.*
import java.lang.ref.WeakReference

class AddRssDialog : MvpAppCompatDialogFragment() {

    companion object {
        private const val MAX_FIELD_LENGTH = 1024

        fun newInstance(url: String): AddRssDialog {
            val dialog = AddRssDialog()
            dialog.mUrl = url

            return dialog
        }
    }

    interface IListener {
        fun onAddRss(url: String)
    }

    private var mListener: WeakReference<IListener>? = null
    private var mUrl: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_add_feed, null)

        view.urlField.filters = Array(1, { _ -> InputFilter.LengthFilter(MAX_FIELD_LENGTH) })
        view.urlField.setText(mUrl ?: "", TextView.BufferType.EDITABLE)

        return AlertDialog.Builder(view.context)
                .setTitle(R.string.add_feed_dialog_title)
                .setView(view)
                .setPositiveButton(R.string.add_feed_dialog_submit_button, { _, _ -> mListener?.get()?.onAddRss(dialog.urlField.text.toString()) })
                .setNegativeButton(R.string.add_feed_dialog_cancel_button, null)
                .create()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is IListener) {
            mListener = WeakReference(context)
        }
    }
}
