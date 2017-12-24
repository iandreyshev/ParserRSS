package ru.iandreyshev.parserrss.presentation.presenter;

import ru.iandreyshev.parserrss.presentation.view.IFeedView;

public interface IFeedViewHost {
    IFeedView getViewState();
}
