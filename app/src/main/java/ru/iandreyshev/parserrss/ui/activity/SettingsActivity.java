package ru.iandreyshev.parserrss.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.iandreyshev.parserrss.presentation.view.ISettingsView;
import ru.iandreyshev.parserrss.presentation.presenter.SettingsPresenter;

import ru.iandreyshev.parserrss.R;

import com.arellomobile.mvp.presenter.InjectPresenter;

public class SettingsActivity extends BaseActivity implements ISettingsView {
    @InjectPresenter
    SettingsPresenter mSettingsPresenter;

    @BindView(R.id.settings_leave_button)
    ImageButton mLeaveButton;

    public static Intent getIntent(final Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    public void openFeed() {
        Intent intent = FeedActivity.getIntent(this)
                .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        mLeaveButton.setOnClickListener(view -> mSettingsPresenter.onLeaveButtonClick());
    }
}
