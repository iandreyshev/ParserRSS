package ru.iandreyshev.parserrss.models.async;

import android.os.AsyncTask;

import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.presentation.view.IFeedView;

public class DeleteFeedTask extends AsyncTask<IViewRss, Void, Void> {
    private IFeedView mFeedViewState;
    private IViewRss mRssToDelete;

    private DeleteFeedTask() {
    }

    public static void execute(final IFeedView feedView, final IViewRss feed) {
        final DeleteFeedTask task = new DeleteFeedTask();
        task.mFeedViewState = feedView;
        task.mRssToDelete = feed;
        task.execute();
    }

    @Override
    protected void onPreExecute() {
        mFeedViewState.startProgressBar(true);
    }

    @Override
    protected Void doInBackground(final IViewRss... rssToDelete) {
        return null;
    }

    @Override
    protected void onPostExecute(final Void result) {
        mFeedViewState.startProgressBar(false);
        mFeedViewState.removeRss(mRssToDelete);
    }
}
