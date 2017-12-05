package ru.iandreyshev.parserrss.ui.activity.article;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.iandreyshev.parserrss.models.article.IArticleInfo;
import ru.iandreyshev.parserrss.presentation.view.article.IArticleView;
import ru.iandreyshev.parserrss.presentation.presenter.article.ArticlePresenter;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.ui.activity.BaseActivity;
import ru.iandreyshev.parserrss.ui.activity.feed.FeedActivity;
import ru.iandreyshev.parserrss.app.util.DataSaver;

import com.arellomobile.mvp.presenter.InjectPresenter;

public class ArticleActivity extends BaseActivity implements IArticleView {
    @InjectPresenter
    ArticlePresenter articlePresenter;

    @BindView(R.id.article_title)
    TextView title;
    @BindView(R.id.article_text)
    TextView text;
    @BindView(R.id.article_date)
    TextView date;
    @BindView(R.id.article_image)
    ImageView image;
    @BindView(R.id.article_button_layout)
    ConstraintLayout backButton;

    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, ArticleActivity.class);

        return intent;
    }

    @Override
    public void openFeed() {
        Intent intent = FeedActivity.getIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
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
        if (!DataSaver.isArticleExist()) {
            openFeed();
        }

        final IArticleInfo article = DataSaver.get();
        title.setText(article.getTitle());
        text.setText(article.getText());

        if (article.isDateExist()) {
            date.setText(article.getDate().toString());
        } else {
            date.setVisibility(View.GONE);
        }

        if (article.isImageExist()) {
            image.setImageBitmap(article.getImage());
        } else {
            image.setVisibility(View.GONE);
        }
    }

    private void initButtonsListeners() {
        backButton.setOnClickListener(view -> articlePresenter.onBackButtonClick());
    }
}
