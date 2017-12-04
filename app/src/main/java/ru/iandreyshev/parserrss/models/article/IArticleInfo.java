package ru.iandreyshev.parserrss.models.article;

import java.util.Date;

public interface IArticleInfo {
    int getId();
    Date getDate();
    String getTitle();
    String getText();
    String getImage();
    boolean isDateExist();
    boolean isImageExist();
}
