package ru.iandreyshev.parserrss.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.arellomobile.mvp.MvpAppCompatDialogFragment;

import ru.iandreyshev.parserrss.R;

public class AddRssDialog extends MvpAppCompatDialogFragment implements DialogInterface.OnClickListener {
    private IOnSubmitAddListener mOnSubmitListener;
    private EditText mField;

    public AddRssDialog setOnSubmitListener(IOnSubmitAddListener listener) {
        mOnSubmitListener = listener;

        return this;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.add_feed_dialog, null);
        mField = view.findViewById(R.id.add_feed_dialog_url_field);

        final Dialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(R.string.add_feed_dialog_submit_button, this)
                .setNegativeButton(R.string.add_feed_dialog_cancel_button, null)
                .create();

        setCancelable(false);

        return dialog;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (mOnSubmitListener != null) {
            mOnSubmitListener.onAddSubmit(dialog, mField.getText().toString());
        }
    }
}
