package ru.iandreyshev.parserrss.models.async;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.iandreyshev.parserrss.app.IEvent;
import ru.iandreyshev.parserrss.models.database.DbFacade;
import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.models.rss.Rss;

public class GetRssFromDbTask extends Task<Void, Void, ArrayList<IViewRss>> {
    private static final String TAG = GetRssFromDbTask.class.getName();

    private final DbFacade mDatabase = new DbFacade();
    private final ArrayList<IViewRss> mResult = new ArrayList<>();
    private IEventListener mListener;
    private IEvent mResultEvent;

    private GetRssFromDbTask() {
    }

    public static void execute(final IEventListener listener) {
        final GetRssFromDbTask task = new GetRssFromDbTask();
        task.setTaskListener(listener);
        task.mListener = listener;
        task.execute();
    }

    @Override
    protected ArrayList<IViewRss> doInBackground(Void... voids) {
        try {
            final List<Rss> rssFromDb = mDatabase.getAllRss();

            for (final Rss rss : rssFromDb) {
                rss.setArticles(mDatabase.getArticles(rss));
                mResult.add(rss);
            }

            mResultEvent = () -> mListener.onSuccess(mResult);

        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            mResultEvent = () -> mListener.onLoadError();

            return null;
        }

        return mResult;
    }

    @Override
    protected void onPostExecute(ArrayList<IViewRss> result) {
        super.onPostExecute(result);
        mResultEvent.doEvent();
    }

    public interface IEventListener extends ITaskListener<ArrayList<IViewRss>> {
        void onLoadError();

        void onSuccess(final ArrayList<IViewRss> rssFromDb);
    }
}
