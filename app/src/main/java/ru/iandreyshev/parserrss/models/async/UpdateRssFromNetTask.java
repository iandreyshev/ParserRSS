package ru.iandreyshev.parserrss.models.async;

import android.util.Log;

import ru.iandreyshev.parserrss.app.Utils;
import ru.iandreyshev.parserrss.models.repository.Database;
import ru.iandreyshev.parserrss.models.repository.Rss;

public final class UpdateRssFromNetTask extends GetRssFromNetTask {
    private final static String TAG = UpdateRssFromNetTask.class.getName();

    private final Database mDatabase = new Database();
    private IEventListener mListener;

    public static void execute(final IEventListener listener, final String url) {
        new UpdateRssFromNetTask(listener, url).executeOnExecutor(TaskExecutor.getInstance());
    }

    public interface IEventListener extends GetRssFromNetTask.IEventListener {
        void onRssNotExist();

        void onDatabaseError();
    }

    @Override
    protected boolean isUrlValid() {
        if (!super.isUrlValid()) {
            setResultEvent(() -> mListener.onInvalidUrl());

            return false;

        } else if (mDatabase.getRssCount(getUrl()) == 0) {
            setResultEvent(() -> mListener.onRssNotExist());

            return false;
        }

        return true;
    }

    @Override
    protected Rss onSuccess(final Rss rss) {
        try {

            if (mDatabase.updateRssWithSameUrl(rss)) {
                rss.setArticles(Utils.sortByDateDESC(rss.getArticles()));
                setResultEvent(() -> mListener.onSuccess(rss));
            } else {
                setResultEvent(() -> mListener.onRssNotExist());
            }

        } catch (final Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            setResultEvent(() -> mListener.onDatabaseError());
        }

        return null;
    }

    private UpdateRssFromNetTask(final IEventListener listener, final String url) {
        super(listener, url);
        mListener = listener;
    }
}
