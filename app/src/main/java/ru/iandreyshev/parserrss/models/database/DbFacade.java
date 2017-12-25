package ru.iandreyshev.parserrss.models.database;

import android.support.annotation.NonNull;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.exception.DbException;
import ru.iandreyshev.parserrss.app.App;
import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.rss.RssArticle;
import ru.iandreyshev.parserrss.models.rss.RssArticle_;
import ru.iandreyshev.parserrss.models.rss.Rss_;

public class DbFacade {
    private static final String TAG = DbFacade.class.getName();

    private final BoxStore mBoxStore = App.getBoxStore();
    private Box<Rss> mRssBox;
    private Box<RssArticle> mArticleBox;

    public DbFacade() throws DbException {
        if (mBoxStore == null) {
            throw new DbException("Box-store is null in constructor");
        }

        mRssBox = mBoxStore.boxFor(Rss.class);
        mArticleBox = mBoxStore.boxFor(RssArticle.class);
    }

    @NonNull
    public List<RssArticle> getArticles(final Rss rss) throws Exception {
        return mArticleBox.query()
                .equal(RssArticle_.mRssId, rss.getId())
                .build()
                .find();
    }

    @NonNull
    public List<Rss> getAllRss() throws Exception {
        return mRssBox.getAll();
    }

    public long getRssCount() throws Exception {
        return mRssBox.count();
    }

    public long getRssCount(final String url) throws Exception {
        return mRssBox.find(Rss_.mUrl, url).size();
    }

    public boolean putRssIfNotExist(final Rss rss) throws Exception {
        return mBoxStore.callInTx(() -> {
            final List<Rss> rssWithSameUrl = mRssBox.find(Rss_.mUrl, rss.getUrl());

            if (!rssWithSameUrl.isEmpty()) {
                return false;
            }

            mRssBox.put(rss);

            return true;
        });
    }

    public void putArticles(final Rss rss, final List<RssArticle> articles) throws Exception {
        mBoxStore.runInTx(() -> {
            mArticleBox.query()
                    .equal(RssArticle_.mRssId, rss.getId())
                    .build()
                    .remove();

            for (final RssArticle article : articles) {
                article.setRssId(rss.getId());
                mArticleBox.put(article);
            }
        });
    }

    public void removeRss(long id) throws Exception {
        mRssBox.remove(id);
    }
}
