package ru.iandreyshev.parserrss.models.database;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.exception.DbException;
import ru.iandreyshev.parserrss.app.App;
import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.rss.RssArticle;
import ru.iandreyshev.parserrss.models.rss.Rss_;

public class RssDatabase {
    private static final String TAG = RssDatabase.class.getName();

    private final BoxStore mBoxStore = App.getBoxStore();
    private Box<Rss> mRssBox;
    private Box<RssArticle> mArticleBox;

    public RssDatabase() throws DbException {
        if (mBoxStore == null) {
            throw new DbException("Box-store is null in constructor");
        }

        mRssBox = mBoxStore.boxFor(Rss.class);
        mArticleBox = mBoxStore.boxFor(RssArticle.class);
    }

    public List<Rss> getAllRss() throws Exception {
        Log.e(TAG, String.format("During get all, articles count is %s", mArticleBox.count()));

        final ArrayList<Rss> result = new ArrayList<>();

        for (final Rss rss : mBoxStore.boxFor(Rss.class).getAll()) {
            result.add(rss);
            Log.e(TAG, String.format("Load rss with %s articles", rss.getArticles().size()));
        }

        return result;
    }

    public long getRssCountByUrl(final String url) throws Exception {
        return mRssBox.find(Rss_.mUrl, url).size();
    }

    public boolean putRssIfSameUrlNotExist(final Rss rss) throws Exception {
        return mBoxStore.callInTx(() -> {
            final Rss rssWithSameUrl = mRssBox.query()
                    .equal(Rss_.mUrl, rss.getUrl())
                    .build()
                    .findFirst();

            if (rssWithSameUrl != null) {
                return false;
            }

            mRssBox.put(rss);

            return true;
        });
    }

    public boolean updateRssIfExistByUrl(final Rss newRss) throws Exception {
        return mBoxStore.callInTx(() -> {
            final Rss currentRss = mRssBox.query()
                    .equal(Rss_.mUrl, newRss.getUrl())
                    .build()
                    .findFirst();

            if (currentRss == null) {
                return false;
            }

            newRss.setId(currentRss.getId());
            mRssBox.put(newRss);

            return true;
        });
    }

    public void removeRss(long id) throws Exception {
        mRssBox.remove(id);
    }
}
