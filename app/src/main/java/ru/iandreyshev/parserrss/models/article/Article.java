package ru.iandreyshev.parserrss.models.article;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.util.Date;

public class Article implements IArticleInfo {
    private int id;
    private Date date;
    private String title;
    private String text;
    private Bitmap image;

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

    public void setDate(Date value) {
        this.date = value;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String value) {
        this.text = value;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    @Override
    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap value) {
        this.image = value;
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
