package ru.iandreyshev.parserrss.app.util;

import android.support.annotation.NonNull;

import ru.iandreyshev.parserrss.models.article.IArticleInfo;

public class DataSaver {
    private static IArticleInfo item;

    public static void save(@NonNull IArticleInfo item) {
        DataSaver.item = item;
    }

    public static IArticleInfo get() {
        return item;
    }

    public static boolean isArticleExist() {
        return item != null;
    }

    private DataSaver() {
    }
}
