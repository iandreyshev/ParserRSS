package ru.iandreyshev.parserrss.presentation.presenter.feed;

import android.os.AsyncTask;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.app.App;
import ru.iandreyshev.parserrss.models.article.Article;
import ru.iandreyshev.parserrss.models.article.IArticleInfo;
import ru.iandreyshev.parserrss.models.feed.Feed;
import ru.iandreyshev.parserrss.presentation.view.feed.FeedView;
import ru.iandreyshev.parserrss.app.util.DataSaver;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class FeedPresenter extends MvpPresenter<FeedView> {
    public void onSettingsButtonClick() {
        getViewState().openSettings();
    }

    public void onRefresh() {
        getViewState().clearFeed();

        for (int i = 0; i < 10; ++i) {
            getViewState().addArticle(new Article(
                    0,
                    "Article name",
                    "Article text. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
            ));
        }

        getViewState().setRefreshing(false);
    }

    public void onItemClick(IArticleInfo article) {
        DataSaver.save(article);
        getViewState().openArticle();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().addFeed(new Feed(0, "Feed name", ""));
        onRefresh();
    }
}
