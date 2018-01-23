package ru.iandreyshev.parserrss.models.async;

import android.util.Log;

import ru.iandreyshev.parserrss.app.App;
import ru.iandreyshev.parserrss.models.filters.IArticlesFilter;
import ru.iandreyshev.parserrss.models.repository.Database;
import ru.iandreyshev.parserrss.models.repository.Rss;
import ru.iandreyshev.parserrss.models.rss.ViewRss;

public final class InsertNewRssTask extends GetRssFromNetTask {
    private final static String TAG = InsertNewRssTask.class.getName();

    private final Database mDatabase = App.getDatabase();
    private final IEventListener mListener;
    private final IArticlesFilter mFilter;

    public static void execute(final IEventListener listener, final String url, final IArticlesFilter filter) {
        new InsertNewRssTask(listener, url, filter)
                .executeOnExecutor(TaskExecutor.getMultiThreadPool());
    }

    public interface IEventListener extends GetRssFromNetTask.IEventListener {
        void onRssAlreadyExist();

        void onDatabaseError();
    }

    @Override
    protected boolean isUrlValid() {
        if (!super.isUrlValid()) {
            setResultEvent(mListener::onInvalidUrl);

            return false;

        } else if (mDatabase.isRssWithUrlExist(getUrl())) {
            setResultEvent(mListener::onRssAlreadyExist);

            return false;
        }

        return true;
    }

    @Override
    protected Rss onSuccess(final Rss rss) {
        try {

            if (mDatabase.putRssIfSameUrlNotExist(rss)) {
                rss.setArticles(mFilter.sort(rss.getArticles()));
                setResultEvent(() -> mListener.onSuccess(new ViewRss(rss)));
            } else {
                setResultEvent(mListener::onRssAlreadyExist);
            }

        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            setResultEvent(mListener::onDatabaseError);
        }

        return rss;
    }

    private InsertNewRssTask(final IEventListener listener, final String url, final IArticlesFilter filter) {
        super(listener, url);
        mListener = listener;
        mFilter = filter;
    }
}
