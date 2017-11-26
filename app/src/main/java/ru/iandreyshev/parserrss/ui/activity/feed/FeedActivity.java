package ru.iandreyshev.parserrss.ui.activity.feed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import ru.iandreyshev.parserrss.presentation.view.feed.FeedView;
import ru.iandreyshev.parserrss.presentation.presenter.feed.FeedPresenter;
import ru.iandreyshev.parserrss.R;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

public class FeedActivity extends MvpAppCompatActivity implements FeedView {
    public static final String TAG = "FeedActivity";
    @InjectPresenter
    FeedPresenter feedPresenter;

    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, FeedActivity.class);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
    }
}
