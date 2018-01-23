package ru.iandreyshev.parserrss.models.async;

import android.util.Log;

import javax.annotation.Nullable;

import ru.iandreyshev.parserrss.app.App;
import ru.iandreyshev.parserrss.models.filters.IArticlesFilter;
import ru.iandreyshev.parserrss.models.repository.Database;
import ru.iandreyshev.parserrss.models.repository.Rss;
import ru.iandreyshev.parserrss.models.rss.ViewRss;

public final class UpdateRssFromNetTask extends GetRssFromNetTask {
    private final static String TAG = UpdateRssFromNetTask.class.getName();

    private final Database mDatabase = App.getDatabase();
    private final IEventListener mListener;
    private final IArticlesFilter mFilter;

    public static void execute(final IEventListener listener, final String url, final IArticlesFilter filter) {
        new UpdateRssFromNetTask(listener, url, filter)
                .executeOnExecutor(TaskExecutor.getMultiThreadPool());
    }

    public interface IEventListener extends GetRssFromNetTask.IEventListener {
        void onRssNotExist();

        void onDatabaseError();
    }

    @Override
    protected boolean isUrlValid() {
        if (!super.isUrlValid()) {
            setResultEvent(mListener::onInvalidUrl);

            return false;

        } else if (!mDatabase.isRssWithUrlExist(getUrl())) {
            setResultEvent(mListener::onRssNotExist);

            return false;
        }

        return true;
    }

    @Override
    @Nullable
    protected Rss onSuccess(final Rss rss) {
        try {

            if (mDatabase.updateRssWithSameUrl(rss)) {
                rss.setArticles(mFilter.sort(rss.getArticles()));
                setResultEvent(() -> mListener.onSuccess(new ViewRss(rss)));
            } else {
                setResultEvent(mListener::onRssNotExist);
            }

        } catch (final Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            setResultEvent(mListener::onDatabaseError);
        }

        return rss;
    }

    private UpdateRssFromNetTask(final IEventListener listener, final String url, final IArticlesFilter filter) {
        super(listener, url);
        mListener = listener;
        mFilter = filter;
    }
}
