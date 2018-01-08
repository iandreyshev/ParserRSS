package ru.iandreyshev.parserrss.models.async;

import android.util.Log;

import ru.iandreyshev.parserrss.app.Utils;
import ru.iandreyshev.parserrss.models.repository.Database;
import ru.iandreyshev.parserrss.models.repository.Rss;

public final class InsertNewRssTask extends GetRssFromNetTask {
    private final static String TAG = InsertNewRssTask.class.getName();

    private final Database mDatabase = new Database();
    private IEventListener mListener;

    public static void execute(final IEventListener listener, final String url) {
        new InsertNewRssTask(listener, url).executeOnExecutor(TaskExecutor.getInstance());
    }

    public interface IEventListener extends GetRssFromNetTask.IEventListener {
        void onRssAlreadyExist();

        void onDatabaseError();
    }

    @Override
    protected boolean isUrlValid() {
        if (!super.isUrlValid()) {
            setResultEvent(() -> mListener.onInvalidUrl());

            return false;

        } else if (mDatabase.getRssCount(getUrl()) > 0) {
            setResultEvent(() -> mListener.onRssAlreadyExist());

            return false;
        }

        return true;
    }

    @Override
    protected Rss onSuccess(final Rss rss) {
        try {

            if (mDatabase.putRssIfSameUrlNotExist(rss)) {
                rss.setArticles(Utils.sortByDateDESC(rss.getArticles()));
                setResultEvent(() -> mListener.onSuccess(rss));
            } else {
                setResultEvent(() -> mListener.onRssAlreadyExist());
            }

        } catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            setResultEvent(() -> mListener.onDatabaseError());
        }

        return rss;
    }

    private InsertNewRssTask(final IEventListener listener, final String url) {
        super(listener, url);
        mListener = listener;
    }
}
