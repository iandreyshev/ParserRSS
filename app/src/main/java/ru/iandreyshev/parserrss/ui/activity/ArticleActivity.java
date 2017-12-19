package ru.iandreyshev.parserrss.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.iandreyshev.parserrss.models.rss.IRssArticle;
import ru.iandreyshev.parserrss.presentation.view.IArticleView;
import ru.iandreyshev.parserrss.presentation.presenter.ArticlePresenter;

import ru.iandreyshev.parserrss.R;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ArticleActivity extends BaseActivity implements IArticleView {
    public static final String ARTICLE_BOUND_KEY = "Article_to_open";
    public static final String TOOLBAR_TITLE = "Article";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("ddd, dd MMM yyyy HH:mm:ss K", Locale.US);

    private IRssArticle mArticle;

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
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            openFeed();
        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_article);

        ButterKnife.bind(this);

        if (!initArticle()) {
            mArticlePresenter.onErrorLoadArticle();
            return;
        }

        initToolbar();
        initButtonsListeners();
    }

    private boolean initArticle() {
        mArticle = (IRssArticle) getIntent().getSerializableExtra(ARTICLE_BOUND_KEY);

        if (mArticle == null) {
            return false;
        }

        mTitle.setText(Html.fromHtml(mArticle.getTitle()));
        mText.setText(Html.fromHtml(mArticle.getDescription()));

        setViewVisible(mImage, (mArticle.getImage() != null));
        if (mArticle.getImage() != null) {
            mImage.setImageBitmap(mArticle.getImage());
        }

        setViewVisible(mDate, (mArticle.getDate() != null));
        if (mArticle.getDate() != null) {
            mDate.setText(DATE_FORMAT.format(mArticle.getDate()));
        }

        return true;
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(TOOLBAR_TITLE);
    }

    private void initButtonsListeners() {
    }

    public void setViewVisible(View view, boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
}
