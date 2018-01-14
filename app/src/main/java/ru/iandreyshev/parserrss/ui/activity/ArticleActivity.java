package ru.iandreyshev.parserrss.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.iandreyshev.parserrss.app.Utils;
import ru.iandreyshev.parserrss.models.rss.IViewArticle;
import ru.iandreyshev.parserrss.presentation.view.IArticleView;
import ru.iandreyshev.parserrss.presentation.presenter.ArticlePresenter;

import ru.iandreyshev.parserrss.R;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ArticleActivity extends BaseActivity implements IArticleView {
    public static final String ARTICLE_BOUND_KEY = "Article_to_open";
    private static final long DEFAULT_ARTICLE_ID = 0;
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
    Uri mOriginalUrl;

    public static Intent getIntent(final Context context) {
        return new Intent(context, ArticleActivity.class);
    }

    @Override
    public void closeArticle() {
        Intent intent = FeedActivity.getIntent(this)
                .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.article_options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                closeArticle();
                break;
            case R.id.article_option_open_in_browser:
                startActivity(new Intent(Intent.ACTION_VIEW,mOriginalUrl));
                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void initArticle(@NonNull final IViewArticle article) {
        mOriginalUrl = Uri.parse(article.getOriginUrl());
        mTitle.setText(Html.fromHtml(article.getTitle()));
        mText.setText(Html.fromHtml(article.getDescription()));

        setViewVisible(mImage, (article.getImage() != null));
        setViewVisible(mDate, (article.getPostDate() != null));

        if (article.getImage() != null) {
            mImage.setImageBitmap(Utils.toBitmap(article.getImage()));
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

        initToolbar();
        initArticle();
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

    private void initArticle() {
        long articleId = getIntent().getLongExtra(ARTICLE_BOUND_KEY, DEFAULT_ARTICLE_ID);
        mArticlePresenter.setArticleId(articleId);
    }

    private void setViewVisible(View view, boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
}
