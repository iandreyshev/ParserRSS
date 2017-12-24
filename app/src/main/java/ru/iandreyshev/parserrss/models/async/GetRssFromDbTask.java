package ru.iandreyshev.parserrss.models.async;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.iandreyshev.parserrss.models.database.DbFacade;
import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.presentation.presenter.IFeedViewHost;

public class GetRssFromDbTask extends AsyncTask<Void, Void, ArrayList<IViewRss>> {
    private static final String TAG = GetRssFromDbTask.class.getName();
    private static final String LOADING_ERROR = "Loading error";

    private IFeedViewHost mViewHost;

    private GetRssFromDbTask() {
    }

    public static void execute(final IFeedViewHost viewHost) {
        final GetRssFromDbTask task = new GetRssFromDbTask();
        task.mViewHost = viewHost;
        task.execute();
    }

    @Override
    protected void onPreExecute() {
        mViewHost.getViewState().startProgressBar(true);
    }

    @Override
    protected ArrayList<IViewRss> doInBackground(Void... voids) {
        final ArrayList<IViewRss> result = new ArrayList<>();

        try {
            final DbFacade dbFacade = new DbFacade();
            final List<Rss> rssFromDb = dbFacade.getAllRss();

            for (final Rss rss : rssFromDb) {
                rss.setArticles(dbFacade.getArticles(rss));
                result.add(rss);
            }

        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));

            return null;
        }

        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<IViewRss> result) {
        mViewHost.getViewState().startProgressBar(false);

        if (result == null) {
            handleDbError();

            return;
        }

        for (final IViewRss rss : result) {
            mViewHost.getViewState().insertRss(rss);
        }
    }

    private void handleDbError() {
        mViewHost.getViewState().showShortToast(LOADING_ERROR);
    }
}
