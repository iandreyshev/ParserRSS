package ru.iandreyshev.parserrss.models.async;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.iandreyshev.parserrss.app.IEvent;
import ru.iandreyshev.parserrss.models.database.RssDatabase;
import ru.iandreyshev.parserrss.models.rss.IViewRss;

public class GetRssFromDbTask extends Task<Void, Void, List<IViewRss>> {
    private static final String TAG = GetRssFromDbTask.class.getName();

    private final RssDatabase mDatabase = new RssDatabase();
    private final List<IViewRss> mResult = new ArrayList<>();
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
    protected List<IViewRss> doInBackground(Void... voids) {
        try {

            mResult.addAll(mDatabase.getAllRss());
            Log.e(TAG, String.format("Take %s rss feeds", mResult.size()));
            mResultEvent = () -> mListener.onSuccess(mResult);

        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            mResultEvent = () -> mListener.onLoadError();

            return null;
        }

        return mResult;
    }

    @Override
    protected void onPostExecute(List<IViewRss> result) {
        super.onPostExecute(result);
        mResultEvent.doEvent();
    }

    public interface IEventListener extends ITaskListener<List<IViewRss>> {
        void onLoadError();

        void onSuccess(final List<IViewRss> rssFromDb);
    }
}
