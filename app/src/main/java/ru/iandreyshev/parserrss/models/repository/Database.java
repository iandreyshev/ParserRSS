package ru.iandreyshev.parserrss.models.repository;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class Database {
    static final List<Long> INVALID_IDS = new ArrayList<>();

    static {
        INVALID_IDS.add(-1L);
        INVALID_IDS.add(0L);
    }

    private BoxStore mBoxStore;
    private Box<Rss> mRssBox;
    private Box<Article> mArticleBox;

    public Database(@NonNull final BoxStore store) {
        mBoxStore = store;
        mRssBox = mBoxStore.boxFor(Rss.class);
        mArticleBox = mBoxStore.boxFor(Article.class);
    }

    @Nullable
    public Rss getRssById(long id) {
        final Rss rss = getRss(id);

        if (rss == null) {
            return null;
        }

        rss.setArticles(getArticlesByRssId(rss.getId()));

        return rss;
    }

    @Nullable
    public Article getArticleById(long id) {
        return getArticle(id);
    }

    public long[] getRssIdList() throws Exception {
        return mBoxStore.callInTx(() -> mRssBox.query()
                .notNull(Rss_.mId)
                .build()
                .findIds());
    }

    public boolean isRssWithUrlExist(final String url) {
        return !mRssBox.find(Rss_.mUrl, url).isEmpty();
    }

    public void updateArticleImage(long id, Bitmap image) {
        final Article article = getArticle(id);

        if (article == null) {
            return;
        }

        article.setImage(image);
        mArticleBox.put(article);
    }

    @NonNull
    public List<Rss> getAllRss() {
        final List<Rss> result = mRssBox.getAll();

        for (final Rss rss : result) {
            rss.setArticles(getArticlesByRssId(rss.getId()));
        }

        return result;
    }

    public boolean putRssIfSameUrlNotExist(final Rss newRss) throws Exception {
        return mBoxStore.callInTx(() -> {
            final Rss rssWithSameUrl = mRssBox.query()
                    .equal(Rss_.mUrl, newRss.getUrl())
                    .build()
                    .findFirst();

            if (rssWithSameUrl != null) {
                return false;
            }

            mRssBox.put(newRss);
            putArticles(newRss);

            return true;
        });
    }

    public boolean updateRssWithSameUrl(final Rss newRss) throws Exception {
        return mBoxStore.callInTx(() -> {
            final Rss rssWithSameUrl = mRssBox.query()
                    .equal(Rss_.mUrl, newRss.getUrl())
                    .build()
                    .findFirst();

            if (rssWithSameUrl == null) {
                return false;
            }

            newRss.setId(rssWithSameUrl.getId());
            mRssBox.put(newRss);
            putArticles(newRss);

            return true;
        });
    }

    public String getRssTitle(long id) {
        return mRssBox.get(id).mTitle;
    }

    public void removeRssById(long id) {
        if (INVALID_IDS.contains(id)) {
            return;
        }

        mBoxStore.runInTx(() -> {
            mRssBox.remove(id);
            mArticleBox.query()
                    .equal(Article_.mRssId, id)
                    .build()
                    .remove();
        });
    }

    private void putArticles(final Rss rss) {
        bindArticles(rss);

        final HashSet<Article> newArticles = new HashSet<>(rss.getArticles());
        final List<Article> currentArticles = getArticlesByRssId(rss.getId());

        for (final Article article : currentArticles) {
            if (!newArticles.remove(article)) {
                mArticleBox.remove(article);
            }
        }

        mArticleBox.put(newArticles);
        rss.setArticles(getArticlesByRssId(rss.getId()));
    }

    private void bindArticles(final Rss rss) {
        for (final Article article : rss.getArticles()) {
            article.setRssId(rss.getId());
        }
    }

    @NonNull
    private List<Article> getArticlesByRssId(long id) {
        if (INVALID_IDS.contains(id)) {
            return new ArrayList<>();
        }

        return mArticleBox.query()
                .equal(Article_.mRssId, id)
                .build()
                .find();
    }

    @Nullable
    private Rss getRss(long id) {
        if (INVALID_IDS.contains(id)) {
            return null;
        }

        return mRssBox.get(id);
    }

    @Nullable
    private Article getArticle(long id) {
        if (INVALID_IDS.contains(id)) {
            return null;
        }

        return mArticleBox.get(id);
    }
}
