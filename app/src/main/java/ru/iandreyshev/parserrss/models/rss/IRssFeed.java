package ru.iandreyshev.parserrss.models.rss;

import ru.iandreyshev.parserrss.models.web.Url;

public interface IRssFeed {
    String getTitle();

    String getDescription();

    String getOrigin();

    Url getUrl();
}
