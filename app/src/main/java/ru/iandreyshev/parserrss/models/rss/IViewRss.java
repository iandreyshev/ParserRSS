package ru.iandreyshev.parserrss.models.rss;

import java.io.Serializable;
import java.util.ArrayList;

public interface IViewRss extends Serializable {
    long getId();

    String getTitle();

    String getDescription();

    String getUrl();

    String getOrigin();

    ArrayList<IViewRssArticle> getArticles();
}
