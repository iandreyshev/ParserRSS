package ru.iandreyshev.parserrss.models.async;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.iandreyshev.parserrss.app.IEvent;
import ru.iandreyshev.parserrss.models.repository.Database;
import ru.iandreyshev.parserrss.models.rss.IViewRss;

public final class GetAllRssFromDbTask extends Task<Void, Void, List<IViewRss>> {
    private static final String TAG = GetAllRssFromDbTask.class.getName();

    private final Database mDatabase = new Database();
    private final List<IViewRss> mResult = new ArrayList<>();
    private IEventListener mListener;
    private IEvent mResultEvent;

    public static void execute(final IEventListener listener) {
        final GetAllRssFromDbTask task = new GetAllRssFromDbTask();
        task.setTaskListener(listener);
        task.mListener = listener;
        task.execute();
    }

    public interface IEventListener extends ITaskListener<List<IViewRss>> {
        void onLoadError();

        void onSuccess(final List<IViewRss> rssFromDb);
    }

    @NonNull
    @Override
    protected List<IViewRss> doInBackground(Void... voids) {
        try {

            mResult.addAll(mDatabase.getAllRss());
            Log.e(TAG, String.format("Take %s rss feeds", mResult.size()));
            mResultEvent = () -> mListener.onSuccess(mResult);

        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            mResultEvent = () -> mListener.onLoadError();
        }

        return mResult;
    }

    @Override
    protected void onPostExecute(List<IViewRss> result) {
        super.onPostExecute(result);
        mResultEvent.doEvent();
    }

    private GetAllRssFromDbTask() {
    }
}
