package ru.iandreyshev.parserrss.models.feed;

import java.util.Date;

public interface IFeedItem {
    int getId();
    String getTitle();
    String getText();
    Date getDate();
}
