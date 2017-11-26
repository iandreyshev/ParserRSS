package ru.iandreyshev.parserrss.ui.activities.article;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import ru.iandreyshev.parserrss.presentation.view.article.ArticleView;
import ru.iandreyshev.parserrss.presentation.presenter.article.ArticlePresenter;

import ru.iandreyshev.parserrss.R;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

public class ArticleActivity extends MvpAppCompatActivity implements ArticleView {
    @InjectPresenter
    ArticlePresenter mArticlePresenter;

    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, ArticleActivity.class);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
    }
}
