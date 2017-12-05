package ru.iandreyshev.parserrss.presentation.presenter.article;


import ru.iandreyshev.parserrss.presentation.view.article.IArticleView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class ArticlePresenter extends MvpPresenter<IArticleView> {
    public void onBackButtonClick() {
        getViewState().openFeed();
    }
}
