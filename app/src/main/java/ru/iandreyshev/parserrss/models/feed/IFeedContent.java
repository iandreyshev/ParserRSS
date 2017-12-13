package ru.iandreyshev.parserrss.models.feed;

import ru.iandreyshev.parserrss.models.web.Url;

public interface IFeedContent {
    String getTitle();

    String getDescription();

    String getOrigin();
}
