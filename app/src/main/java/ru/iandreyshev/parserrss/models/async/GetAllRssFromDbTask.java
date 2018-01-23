package ru.iandreyshev.parserrss.models.async;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.iandreyshev.parserrss.app.App;
import ru.iandreyshev.parserrss.models.filters.IArticlesFilter;
import ru.iandreyshev.parserrss.models.repository.Rss;
import ru.iandreyshev.parserrss.models.rss.ViewRss;

public final class GetAllRssFromDbTask extends Task<Void, Void, List<ViewRss>> {
    private static final String TAG = GetAllRssFromDbTask.class.getName();

    private final IEventListener mListener;
    private final IArticlesFilter mFilter;

    public static void execute(final IEventListener listener, final IArticlesFilter filter) {
        new GetAllRssFromDbTask(listener, filter)
                .executeOnExecutor(TaskExecutor.getMultiThreadPool());
    }

    public interface IEventListener extends ITaskListener<List<ViewRss>> {
        void onLoadError();

        void onSuccess(final List<ViewRss> rssFromDb);
    }

    @NonNull
    @Override
    protected List<ViewRss> doInBackground(Void... voids) {
        final List<ViewRss> result = new ArrayList<>();

        try {

            for (final Rss rss : App.getDatabase().getAllRss()) {
                rss.setArticles(mFilter.sort(rss.getArticles()));
                result.add(new ViewRss(rss));
            }
            setResultEvent(() -> mListener.onSuccess(result));

        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            setResultEvent(mListener::onLoadError);
        }

        return result;
    }

    private GetAllRssFromDbTask(final IEventListener listener, final IArticlesFilter filter) {
        super(listener);
        mListener = listener;
        mFilter = filter;
    }
}
