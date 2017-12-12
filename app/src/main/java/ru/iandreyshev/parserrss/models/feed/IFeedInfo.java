package ru.iandreyshev.parserrss.models.feed;

import okhttp3.HttpUrl;

public interface IFeedInfo {
    int getId();

    String getTitle();

    String getDescription();

    HttpUrl getOriginUrl();
}
