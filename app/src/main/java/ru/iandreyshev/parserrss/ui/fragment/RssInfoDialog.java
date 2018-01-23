package ru.iandreyshev.parserrss.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatDialogFragment;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.models.rss.ViewRss;

public class RssInfoDialog extends MvpAppCompatDialogFragment {
    private ViewRss mRss;

    public static void show(final FragmentManager fragmentManager, @NonNull final ViewRss rss) {
        final RssInfoDialog dialog = new RssInfoDialog();
        dialog.mRss = rss;
        dialog.show(fragmentManager, RssInfoDialog.class.getName());
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(final Bundle savedInstantState) {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.rss_info_dialog, null);
        initViewContent(view);

        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
                .setView(view)
                .setPositiveButton(R.string.rss_info_dialog_button, null)
                .setNeutralButton(R.string.rss_info_open_original_button, this::onOpenOriginalButtonClick);

        return builder.create();
    }

    private void initViewContent(final View view) {
        final TextView title = view.findViewById(R.id.rss_info_title);
        title.setText(mRss.getTitle());

        if (mRss.getDescription() != null) {
            final TextView description = view.findViewById(R.id.rss_info_description);
            description.setText(mRss.getDescription());
        }
    }

    private void onOpenOriginalButtonClick(DialogInterface dialog, int which) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mRss.getOrigin())));
    }
}
