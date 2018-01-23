package ru.iandreyshev.parserrss.ui.fragment;

import android.app.Dialog;
import android.support.v4.app.FragmentManager;
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
import ru.iandreyshev.parserrss.ui.listeners.IOnSubmitAddRssListener;

public class AddRssDialog extends MvpAppCompatDialogFragment {
    private IOnSubmitAddRssListener mOnSubmitListener;
    private EditText mField;

    public static void show(final FragmentManager fragmentManager) {
        new AddRssDialog().show(fragmentManager, AddRssDialog.class.getName());
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.add_feed_dialog, null);
        mField = view.findViewById(R.id.add_feed_dialog_url_field);

        return new AlertDialog.Builder(view.getContext())
                .setView(view)
                .setPositiveButton(R.string.add_feed_dialog_submit_button, this::onAddButtonClick)
                .setNegativeButton(R.string.add_feed_dialog_cancel_button, null)
                .create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnSubmitListener = (FeedActivity) context;
    }

    private void onAddButtonClick(DialogInterface dialog, int which) {
        if (mOnSubmitListener != null) {
            mOnSubmitListener.onSubmitAddRss(mField.getText().toString());
        }
    }
}
