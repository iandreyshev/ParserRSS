package ru.iandreyshev.parserrss.ui.activities.feed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;

import butterknife.ButterKnife;
import ru.iandreyshev.parserrss.presentation.view.feed.FeedView;
import ru.iandreyshev.parserrss.presentation.presenter.feed.FeedPresenter;
import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.ui.activities.article.ArticleActivity;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

public class FeedActivity extends MvpAppCompatActivity implements FeedView {
    @InjectPresenter
    FeedPresenter feedPresenter;

    @BindView(R.id.feed_name)
    TextView feedName;
    @BindView(R.id.settings_button)
    ImageButton settingsButton;

    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, FeedActivity.class);

        return intent;
    }

    @Override
    public void setFeedName(final String value) {
        feedName.setText(value);
    }

    @Override
    public void openSettings() {
        final Intent intent = new Intent(this, ArticleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.bind(this);

        settingsButton.setOnClickListener(view -> feedPresenter.openSettings());
    }
}
