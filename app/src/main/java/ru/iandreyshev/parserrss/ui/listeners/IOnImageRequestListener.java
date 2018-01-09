package ru.iandreyshev.parserrss.ui.listeners;

import ru.iandreyshev.parserrss.models.rss.IViewArticle;

public interface IOnImageRequestListener {
    void loadImage(IViewArticle article, IOnImageInsertListener insertListener);
}
