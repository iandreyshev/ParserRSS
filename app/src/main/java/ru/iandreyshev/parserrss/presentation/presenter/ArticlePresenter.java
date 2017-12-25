package ru.iandreyshev.parserrss.presentation.presenter;


import ru.iandreyshev.parserrss.presentation.view.IArticleView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class ArticlePresenter extends MvpPresenter<IArticleView> {
    public void onErrorLoadArticle() {
        // TODO: Create redirect to feed with message
    }
}
