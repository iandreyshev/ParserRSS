package ru.iandreyshev.parserrss.util;

import android.support.annotation.NonNull;

import ru.iandreyshev.parserrss.models.feed.IFeedItem;

public class FeedItemPref {
    private static IFeedItem item;

    public static void save(@NonNull  IFeedItem item) {
        FeedItemPref.item = item;
    }

    public static IFeedItem get() {
        return item;
    }

    public static boolean isSaved() {
        return item != null;
    }

    private FeedItemPref() {
    }
}
