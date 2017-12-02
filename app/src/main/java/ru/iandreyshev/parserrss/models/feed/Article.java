package ru.iandreyshev.parserrss.models.feed;

import android.support.annotation.NonNull;

import java.util.Date;

public class Article implements IFeedItem {
    private int id;
    private String title;
    private String text;
    private Date date;

    public Article(int id, @NonNull String title, @NonNull String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public Date getDate() {
        return this.date;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }
}
