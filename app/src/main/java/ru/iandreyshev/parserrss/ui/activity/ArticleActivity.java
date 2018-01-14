package ru.iandreyshev.parserrss.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import ru.iandreyshev.parserrss.presentation.presenter.ImagesLoadPresenter;
import ru.iandreyshev.parserrss.presentation.view.IArticleView;
import ru.iandreyshev.parserrss.presentation.presenter.ArticlePresenter;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.presentation.view.IImageView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ArticleActivity extends BaseActivity implements IArticleView, IImageView {
    public static final String ARTICLE_BOUND_KEY = "Article_to_open";
    private static final long DEFAULT_ARTICLE_ID = 0;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);

    @InjectPresenter
    ArticlePresenter mArticlePresenter;
    @InjectPresenter(type = PresenterType.GLOBAL, tag = "ImageLoadPresenter")
    ImagesLoadPresenter mImageLoadPresenter;

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

    private IViewArticle mArticle;

    public static Intent getIntent(final Context context) {
        return new Intent(context, ArticleActivity.class);
    }

    @Override
    public void closeArticle() {
        Intent intent = FeedActivity.getIntent(this)
                .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NO_HISTORY);

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
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mArticle.getOriginUrl())));
                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void initArticle(@NonNull final IViewArticle article, @NonNull final String rssName) {
        mArticle = article;
        mTitle.setText(Html.fromHtml(article.getTitle()));
        mText.setText(Html.fromHtml(article.getDescription()));
        loadImage(Utils.toBitmap(article.getImage()));
        loadDate(article.getPostDate());

        mToolbar.setTitle(mArticle.getTitle());
        mToolbar.setSubtitle(rssName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_article);

        ButterKnife.bind(this);

        initToolbar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initArticle();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() == null) {
            return;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initArticle() {
        mArticlePresenter.setArticleId(getIntent().getLongExtra(ARTICLE_BOUND_KEY, DEFAULT_ARTICLE_ID));
    }

    private void setViewVisible(View view, boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void loadImage(@Nullable final Bitmap bitmap) {
        setViewVisible(mImage, (bitmap != null));

        if (bitmap != null) {
            mImage.setImageBitmap(bitmap);
        } else {
            mImageLoadPresenter.loadImage(mArticle.getId());
        }
    }

    private void loadDate(@Nullable final Long date) {
        setViewVisible(mDate, (date != null));
        mDate.setText(DATE_FORMAT.format(date));
    }

    @Override
    public void insertImage(long articleId, @NonNull Bitmap imageBitmap) {
        loadImage(imageBitmap);
    }
}
