package ru.iandreyshev.parserrss.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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

public class ArticleActivity extends BaseActivity implements IArticleView {
    public static final String ARTICLE_BOUND_KEY = "Article_to_open";

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_article);

        ButterKnife.bind(this);

        if (!initArticle()) {
            mArticlePresenter.onErrorLoadArticle();
            return;
        }

        setSupportActionBar(mToolbar);
        initButtonsListeners();
    }

    private boolean initArticle() {
        final IRssArticle article = (IRssArticle) getIntent().getSerializableExtra(ARTICLE_BOUND_KEY);

        if (article == null) {
            return false;
        }

        mTitle.setText(Html.fromHtml(article.getTitle()));
        mText.setText(Html.fromHtml(article.getDescription()));

        if (article.getImage() != null) {
            mImage.setImageBitmap(article.getImage());
        } else {
            mImage.setVisibility(View.GONE);
        }

        return true;
    }

    private void initButtonsListeners() {
    }
}
