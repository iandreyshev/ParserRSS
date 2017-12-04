package ru.iandreyshev.parserrss.models.article;

import android.support.annotation.NonNull;

import java.util.Date;

public class Article implements IArticleInfo {
    private int id;
    private Date date;
    private String title;
    private String text;
    private String image;

    public Article(int id, @NonNull String title, @NonNull String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getImage() {
        return image;
    }

    public void setImage(String value) {
        this.image = image;
    }

    @Override
    public boolean isDateExist() {
        return date != null;
    }

    @Override
    public boolean isImageExist() {
        return image != null;
    }
}
