package ru.iandreyshev.parserrss.presentation.presenter.feed;

import android.os.AsyncTask;

import ru.iandreyshev.parserrss.models.article.Article;
import ru.iandreyshev.parserrss.models.article.IArticleInfo;
import ru.iandreyshev.parserrss.models.feed.Feed;
import ru.iandreyshev.parserrss.models.feed.IFeedInfo;
import ru.iandreyshev.parserrss.presentation.presenter.task.RefreshFeedTask;
import ru.iandreyshev.parserrss.presentation.presenter.task.listeners.IOnErrorListener;
import ru.iandreyshev.parserrss.presentation.presenter.task.listeners.IOnSuccessListener;
import ru.iandreyshev.parserrss.presentation.view.feed.IFeedView;
import ru.iandreyshev.parserrss.app.util.DataSaver;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

@InjectViewState
public final class FeedPresenter extends MvpPresenter<IFeedView>
        implements IOnErrorListener<RefreshFeedTask.RefreshError>, IOnSuccessListener<List<IArticleInfo>> {
    private RefreshFeedTask refreshTask = new RefreshFeedTask();

    public void onRefresh(IFeedInfo feed) {
        if (refreshTask.getStatus() == AsyncTask.Status.RUNNING) {
            return;
        } else if (refreshTask.getStatus() == AsyncTask.Status.FINISHED || refreshTask.isCancelled()) {
            refreshTask = new RefreshFeedTask();
        }

        refreshTask
                .setSuccessListener(this)
                .setErrorListener(this)
                .execute(feed);
    }

    public void onSettingsButtonClick() {
        getViewState().openSettings();
    }

    public void onItemClick(IArticleInfo article) {
        DataSaver.save(article);
        getViewState().openArticle();
    }

    @Override
    public void onSuccessEvent(List<IArticleInfo> articleList) {
        getViewState().updateFeedList(new Feed(0, "", ""), articleList);
        getViewState().showShortToast("Refreshed");
    }

    @Override
    public void onErrorEvent(RefreshFeedTask.RefreshError error) {
        switch (error) {
            case BadConnection:
                getViewState().showShortToast("Connection error");
                break;
        }
        getViewState().setRefreshing(false);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        List<IArticleInfo> result = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            result.add(new Article(0, "Article name", "Article text"));
        }

        getViewState().updateFeedList(new Feed(0, "", ""), result);
        getViewState().showShortToast("Refreshed");
    }
}
