package ru.iandreyshev.parserrss.models.database;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.exception.DbException;
import ru.iandreyshev.parserrss.app.App;
import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.rss.RssArticle;
import ru.iandreyshev.parserrss.models.rss.RssArticle_;
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

    public long getRssCount(final String url) {
        return mRssBox.find(Rss_.mUrl, url).size();
    }

    @NonNull
    public List<Rss> getAllRss() throws Exception {
        return mBoxStore.callInTx(() -> {
            Log.e(TAG, String.format("During get all, articles count is %s", mArticleBox.count()));
            final ArrayList<Rss> result = new ArrayList<>();

            for (final Rss rss : mRssBox.getAll()) {
                final List<RssArticle> articles = mArticleBox.query()
                        .equal(RssArticle_.mRssId, rss.getId())
                        .build()
                        .find();
                rss.setArticles(articles);
                result.add(rss);
                Log.e(TAG, String.format("Load rss with %s articles", rss.getViewArticles().size()));
            }

            return result;
        });
    }

    public boolean putRssIfSameUrlNotExist(final Rss rss) throws Exception {
        return mBoxStore.callInTx(() -> {
            Rss rssWithSameUrl = mRssBox.query()
                    .equal(Rss_.mUrl, rss.getUrl())
                    .build()
                    .findFirst();

            if (rssWithSameUrl != null) {
                return false;
            }

            mArticleBox.query()
                    .equal(RssArticle_.mRssId, rss.getId())
                    .build()
                    .remove();
            mRssBox.put(rss);
            rss.bindArticles();
            mArticleBox.put(rss.getArticles());

            return true;
        });
    }

    public boolean updateRssWithSameUrl(final Rss newRss) throws Exception {
        return mBoxStore.callInTx(() -> {
            Rss rssWithSameUrl = mRssBox.query()
                    .equal(Rss_.mUrl, newRss.getUrl())
                    .build()
                    .findFirst();

            if (rssWithSameUrl == null) {
                return false;
            }

            newRss.setId(rssWithSameUrl.getId());
            newRss.bindArticles();
            mRssBox.put(newRss);

            mArticleBox.query()
                    .equal(RssArticle_.mRssId, newRss.getId())
                    .build()
                    .remove();
            mArticleBox.put(newRss.getArticles());

            return true;
        });
    }

    public void removeRss(long rssId) throws Exception {
        mBoxStore.runInTx(() -> {
            mRssBox.remove(rssId);
            mArticleBox.query()
                    .equal(RssArticle_.mRssId, rssId)
                    .build()
                    .remove();
        });
    }
}
