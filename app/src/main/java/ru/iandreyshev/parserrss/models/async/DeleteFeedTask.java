package ru.iandreyshev.parserrss.models.async;

import android.os.AsyncTask;

import ru.iandreyshev.parserrss.models.rss.RssFeed;
import ru.iandreyshev.parserrss.presentation.view.IFeedView;

public class DeleteFeedTask extends AsyncTask<RssFeed, Void, Void> {

    private IFeedView mFeedViewState;
    private RssFeed mFeed;

    public static void execute(final IFeedView feedView, final RssFeed feed) {
        final DeleteFeedTask task = new DeleteFeedTask();
        task.mFeedViewState = feedView;
        task.mFeed = feed;
        task.execute();
    }

    @Override
    protected void onPreExecute() {
        mFeedViewState.startProgressBar(true);
    }

    @Override
    protected Void doInBackground(final RssFeed... feeds) {
        return null;
    }

    @Override
    protected void onPostExecute(final Void result) {
        mFeedViewState.startProgressBar(false);
    }

    private DeleteFeedTask() {
    }
}
