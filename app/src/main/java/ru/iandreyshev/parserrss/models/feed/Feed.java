package ru.iandreyshev.parserrss.models.feed;

import android.support.annotation.NonNull;

public class Feed implements IFeedInfo {
    private int id;
    private String name;
    private String url;

    public Feed(int id, @NonNull String name, @NonNull String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int value) {
        this.id = value;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String value) {
        this.url = value;
    }
}
