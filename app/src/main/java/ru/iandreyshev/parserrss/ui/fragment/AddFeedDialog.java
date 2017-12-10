package ru.iandreyshev.parserrss.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.arellomobile.mvp.MvpAppCompatDialogFragment;

import ru.iandreyshev.parserrss.R;

public class AddFeedDialog extends MvpAppCompatDialogFragment {
    private DialogInterface.OnClickListener mOnSubmit;

    public void setOnSubmitListener(DialogInterface.OnClickListener listener) {
        mOnSubmit = listener;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(R.layout.add_feed_dialog)
                .setPositiveButton(
                        R.string.add_feed_dialog_submit_button,
                        mOnSubmit)
                .setNegativeButton(
                        R.string.add_feed_dialog_cancel_button,
                        null);

        setCancelable(false);

        return builder.create();
    }
}
