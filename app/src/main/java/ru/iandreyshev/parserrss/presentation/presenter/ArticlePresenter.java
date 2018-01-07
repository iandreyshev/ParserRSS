package ru.iandreyshev.parserrss.presentation.presenter;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.app.App;
import ru.iandreyshev.parserrss.presentation.view.IArticleView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class ArticlePresenter extends MvpPresenter<IArticleView> {
    public void onErrorLoadArticle() {
        getViewState().showShortToast(App.getStr(R.string.article_error_load));
        getViewState().openFeed();
    }
}
