package ru.iandreyshev.parserrss.models.rss;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.iandreyshev.parserrss.models.repository.Article;
import ru.iandreyshev.parserrss.models.repository.Rss;

public class ViewRss {
    private final long mId;
    private final String mTitle;
    private final String mDescription;
    private final String mUrl;
    private final String mOriginUrl;
    private final List<ViewArticle> mArticles;

    public ViewRss(@NonNull final Rss rss) {
        mId = rss.getId();
        mTitle = rss.getTitle();
        mDescription = rss.getDescription();
        mUrl = rss.getUrl();
        mOriginUrl = rss.getOrigin();
        mArticles = new ArrayList<>();

        for (final Article article : rss.getArticles()) {
            mArticles.add(new ViewArticle(article));
        }
    }

    public long getId() {
        return mId;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    @NonNull
    public String getUrl() {
        return mUrl;
    }

    @NonNull
    public String getOrigin() {
        return mOriginUrl;
    }

    @NonNull
    public List<ViewArticle> getViewArticles() {
        return mArticles;
    }
}
