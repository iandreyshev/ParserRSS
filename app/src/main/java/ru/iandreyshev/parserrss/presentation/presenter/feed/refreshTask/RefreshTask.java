package ru.iandreyshev.parserrss.presentation.presenter.feed.refreshTask;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.iandreyshev.parserrss.models.article.Article;
import ru.iandreyshev.parserrss.models.article.IArticleInfo;
import ru.iandreyshev.parserrss.models.feed.IFeedInfo;

public class RefreshTask extends AsyncTask<IFeedInfo, Void, List<IArticleInfo>> {
    private IOnErrorListener onErrorListener;
    private IOnSuccessListener onSuccessListener;
    private IFeedInfo feed;
    private RefreshError error = RefreshError.None;

    public RefreshTask setMaxDuration(long duration) {
        return this;
    }

    public RefreshTask setOnErrorListener(IOnErrorListener listener) {
        this.onErrorListener = listener;

        return this;
    }

    public RefreshTask setOnSuccessListener(IOnSuccessListener listener) {
        this.onSuccessListener = listener;

        return this;
    }

    @Override
    protected List<IArticleInfo> doInBackground(IFeedInfo... feedsToRefresh) {
        if (feedsToRefresh.length < 1) {
            throw new IllegalArgumentException("Invalid count of refreshing feeds");
        }

        feed = feedsToRefresh[0];
        List<IArticleInfo> result = new ArrayList<>();

        try {
            Thread.sleep(1000);
        } catch (Exception ex) {}

        if (new Random().nextBoolean()) {
            for (int i = 0; i < 10; ++i) {
                result.add(new Article(0, "Article name", "Article text"));
            }
        } else {
            error = RefreshError.BadConnection;
            cancel(false);
        }

        return result;
    }

    @Override
    protected void onPostExecute(List<IArticleInfo> result) {
        super.onPostExecute(result);
        onSuccessListener.onSuccessEvent(feed, result);
    }

    @Override
    protected void onCancelled() {
        onErrorListener.onErrorEvent(RefreshError.BadConnection);
    }

    public enum RefreshError {
        None,
        BadConnection,
    }
}
