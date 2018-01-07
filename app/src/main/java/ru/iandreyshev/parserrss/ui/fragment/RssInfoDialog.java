package ru.iandreyshev.parserrss.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatDialogFragment;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.models.rss.IViewRss;

public class RssInfoDialog extends MvpAppCompatDialogFragment {
    private IViewRss mRss;

    public static void show(final FragmentManager fragmentManager, @NonNull final IViewRss rss) {
        final RssInfoDialog dialog = new RssInfoDialog();
        dialog.mRss = rss;
        dialog.show(fragmentManager, RssInfoDialog.class.getName());
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(final Bundle savedInstantState) {
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.rss_info_dialog, null);
        initViewContent(view);

        final Dialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(R.string.rss_info_dialog_button, null)
                .create();

        setCancelable(false);

        return dialog;
    }

    private void initViewContent(final View view) {
        final TextView title = view.findViewById(R.id.rss_info_title);
        title.setText(mRss.getTitle());

        if (mRss.getDescription() != null) {
            final TextView description = view.findViewById(R.id.rss_info_description);
            description.setText(mRss.getDescription());
        }
    }
}
