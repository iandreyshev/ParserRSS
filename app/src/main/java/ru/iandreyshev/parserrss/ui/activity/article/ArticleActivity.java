package ru.iandreyshev.parserrss.ui.activity.article;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.iandreyshev.parserrss.models.feed.IFeedItem;
import ru.iandreyshev.parserrss.presentation.view.article.ArticleView;
import ru.iandreyshev.parserrss.presentation.presenter.article.ArticlePresenter;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.ui.activity.feed.FeedActivity;
import ru.iandreyshev.parserrss.util.FeedItemPref;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

public class ArticleActivity extends MvpAppCompatActivity implements ArticleView {
    @InjectPresenter
    ArticlePresenter articlePresenter;

    @BindView(R.id.article_title)
    TextView title;
    @BindView(R.id.article_text)
    TextView text;
    @BindView(R.id.article_back_button)
    ImageButton backButton;

    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, ArticleActivity.class);

        return intent;
    }

    public void openFeed() {
        Intent intent = new Intent(this, FeedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        ButterKnife.bind(this);

        loadContent();
        initButtonsListeners();
    }

    private void loadContent() {
        if (!FeedItemPref.isSaved()) {
            openFeed();
        }

        IFeedItem item = FeedItemPref.get();
        title.setText(item.getTitle());
        text.setText(item.getText());
    }

    private void initButtonsListeners() {
        backButton.setOnClickListener(view -> articlePresenter.onBackButtonClick());
    }
}
