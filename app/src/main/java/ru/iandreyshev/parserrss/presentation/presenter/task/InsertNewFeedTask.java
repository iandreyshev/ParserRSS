package ru.iandreyshev.parserrss.presentation.presenter.task;

import ru.iandreyshev.parserrss.models.feed.IFeedInfo;

public class InsertNewFeedTask extends Task<String, Void, IFeedInfo, InsertNewFeedTask.InsertError> {
    @Override
    protected IFeedInfo doInBackground(String... urlCollection) {
        return null;
    }

    public enum InsertError {
        InvalidUrl,
    }
}
