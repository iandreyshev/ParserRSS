package ru.iandreyshev.parserrss.ui.activity.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.iandreyshev.parserrss.presentation.view.settings.SettingsView;
import ru.iandreyshev.parserrss.presentation.presenter.settings.SettingsPresenter;

import com.arellomobile.mvp.MvpAppCompatActivity;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.ui.activity.feed.FeedActivity;

import com.arellomobile.mvp.presenter.InjectPresenter;

public class SettingsActivity extends MvpAppCompatActivity implements SettingsView {
    @InjectPresenter
    SettingsPresenter settingsPresenter;
    @BindView(R.id.settings_leave_button)
    ImageButton leaveButton;

    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);

        return intent;
    }

    @Override
    public void openFeed() {
        Intent intent = FeedActivity.getIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        leaveButton.setOnClickListener(view -> settingsPresenter.onLeaveButtonClick());
    }
}
