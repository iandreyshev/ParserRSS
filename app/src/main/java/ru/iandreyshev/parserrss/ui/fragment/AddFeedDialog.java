package ru.iandreyshev.parserrss.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.arellomobile.mvp.MvpAppCompatDialogFragment;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.ui.activity.FeedActivity;

public class AddFeedDialog extends MvpAppCompatDialogFragment {
    private IOnSubmitAddingListener mOnSubmitListener;
    private EditText mField;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.add_feed_dialog, null);
        mField = view.findViewById(R.id.add_feed_dialog_url_field);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(
                        R.string.add_feed_dialog_submit_button,
                        (DialogInterface dialogInterface, int w) -> {
                            if (mOnSubmitListener != null) {
                                final String url =  mField.getText().toString();
                                mOnSubmitListener.onSubmit(dialogInterface, url);
                            }
                        })
                .setNegativeButton(
                        R.string.add_feed_dialog_cancel_button,
                        null);

        setCancelable(false);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FeedActivity) {
            mOnSubmitListener = (FeedActivity) context;
        }
    }
}
