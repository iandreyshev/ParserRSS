package ru.iandreyshev.parserrss.presentation.presenter.feed;

import android.os.AsyncTask;

import ru.iandreyshev.parserrss.models.article.IArticleInfo;
import ru.iandreyshev.parserrss.models.feed.IFeedInfo;
import ru.iandreyshev.parserrss.presentation.presenter.feed.refreshTask.IOnSuccessListener;
import ru.iandreyshev.parserrss.presentation.presenter.feed.refreshTask.IOnErrorListener;
import ru.iandreyshev.parserrss.presentation.presenter.feed.refreshTask.RefreshTask;
import ru.iandreyshev.parserrss.presentation.view.feed.IFeedView;
import ru.iandreyshev.parserrss.app.util.DataSaver;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

@InjectViewState
public class FeedPresenter extends MvpPresenter<IFeedView> {
    private static final long REFRESH_TIMEOUT = 1000 * 3;
    private RefreshTaskListener refreshTaskListener = new RefreshTaskListener();
    private RefreshTask refreshTask = new RefreshTask();

    public void onRefresh(IFeedInfo feed) {
        if (refreshTask.getStatus() == AsyncTask.Status.RUNNING) {
            return;
        } else if (refreshTask.getStatus() == AsyncTask.Status.FINISHED || refreshTask.isCancelled()) {
            refreshTask = new RefreshTask();
        }

        refreshTask
                .setOnSuccessListener(refreshTaskListener)
                .setOnErrorListener(refreshTaskListener)
                .setMaxDuration(REFRESH_TIMEOUT)
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
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    private class RefreshTaskListener implements IOnSuccessListener, IOnErrorListener {
        @Override
        public void onErrorEvent(RefreshTask.RefreshError error) {
            switch (error) {
                case BadConnection:
                    getViewState().showShortToast("Connection error");
                    break;
            }
            getViewState().setRefreshing(false);
        }

        @Override
        public void onSuccessEvent(IFeedInfo feed, List<IArticleInfo> result) {
            getViewState().updateFeedList(feed, result);
            getViewState().showShortToast("Refreshed!");
        }
    }
}
