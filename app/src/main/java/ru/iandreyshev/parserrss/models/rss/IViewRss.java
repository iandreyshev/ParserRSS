package ru.iandreyshev.parserrss.models.rss;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public interface IViewRss extends Serializable {
    long getId();

    @NonNull
    String getTitle();

    String getDescription();

    @NonNull
    String getUrl();

    @NonNull
    String getOrigin();

    @NonNull
    List<IViewArticle> getViewArticles();
}
