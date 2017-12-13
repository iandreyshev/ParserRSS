package ru.iandreyshev.parserrss.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.iandreyshev.parserrss.models.article.IArticleContent;
import ru.iandreyshev.parserrss.presentation.view.IArticleView;
import ru.iandreyshev.parserrss.presentation.presenter.ArticlePresenter;

import ru.iandreyshev.parserrss.R;

import com.arellomobile.mvp.presenter.InjectPresenter;

public class ArticleActivity extends BaseActivity implements IArticleView {
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
    @BindView(R.id.article_button_layout)
    ConstraintLayout mBackButton;

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
        setContentView(R.layout.activity_article);

        ButterKnife.bind(this);

        if (!isIntentContainsArticle()) {
            mArticlePresenter.onErrorLoadArticle();
            return;
        }

        initContent(getArticleFromIntent());
        initButtonsListeners();
    }

    private boolean isIntentContainsArticle() {
        IArticleContent article;

        try {
            article = getArticleFromIntent();
        } catch (Exception ex) {
            return false;
        }

        return article != null;
    }

    private void initContent(IArticleContent article) {
        mTitle.setText(article.getTitle());
        mText.setText(article.getText());

        if (article.isDateExist()) {
            mDate.setText(article.getDate().toString());
        } else {
            mDate.setVisibility(View.GONE);
        }

        if (article.isImageExist()) {
            mImage.setImageBitmap(article.getImage());
        } else {
            mImage.setVisibility(View.GONE);
        }
    }

    private IArticleContent getArticleFromIntent() {
        final String key = getResources().getString(R.string.const_article_bundle_key);
        return (IArticleContent) getIntent().getSerializableExtra(key);
    }

    private void initButtonsListeners() {
        mBackButton.setOnClickListener(view -> mArticlePresenter.onBackButtonClick());
    }
}
