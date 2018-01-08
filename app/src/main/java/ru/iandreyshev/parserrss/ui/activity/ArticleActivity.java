package ru.iandreyshev.parserrss.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.iandreyshev.parserrss.models.rss.IViewArticle;
import ru.iandreyshev.parserrss.presentation.view.IArticleView;
import ru.iandreyshev.parserrss.presentation.presenter.ArticlePresenter;

import ru.iandreyshev.parserrss.R;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ArticleActivity extends BaseActivity implements IArticleView {
    public static final String ARTICLE_BOUND_KEY = "Article_to_open";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
    private static final String TAG = ArticleActivity.class.getName();

    @InjectPresenter
    ArticlePresenter mArticlePresenter;

    @BindView(R.id.article_title)
    TextView mTitle;
    @BindView(R.id.article_text)
    TextView mText;
    @BindView(R.id.article_date)
    TextView mDate;
    @BindView(R.id.article_image)
    ImageView mImage;
    @BindView(R.id.article_toolbar)
    Toolbar mToolbar;

    public static Intent getIntent(final Context context) {
        return new Intent(context, ArticleActivity.class);
    }

    @Override
    public void openFeed() {
        Intent intent = FeedActivity.getIntent(this)
                .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
    }

    @Override
    public void startProgressBar(boolean isStart) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            openFeed();
        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void initArticle(@NonNull final IViewArticle article) {
        mTitle.setText(Html.fromHtml(article.getTitle()));
        mText.setText(Html.fromHtml(article.getDescription()));

        setViewVisible(mImage, (article.getImage() != null));
        setViewVisible(mDate, (article.getPostDate() != null));

        if (article.getImage() != null) {
            mImage.setImageBitmap(article.getImage());
        }
        if (article.getPostDate() != null) {
            mDate.setText(DATE_FORMAT.format(article.getPostDate()));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_article);

        ButterKnife.bind(this);

        final Bundle extras = getIntent().getExtras();

        if (extras == null) {
            mArticlePresenter.onErrorLoadArticle();

            return;
        }

        initToolbar();
        mArticlePresenter.onLoadArticle(extras.getLong(ARTICLE_BOUND_KEY));
    }

    private void initToolbar() {
        mToolbar.setTitle(getString(R.string.article_toolbar_title));
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() == null) {
            return;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setViewVisible(View view, boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
}
