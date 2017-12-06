package ru.iandreyshev.parserrss.presentation.presenter.settings;

import android.os.AsyncTask;

import ru.iandreyshev.parserrss.models.feed.IFeedInfo;
import ru.iandreyshev.parserrss.presentation.presenter.task.listeners.IOnErrorListener;
import ru.iandreyshev.parserrss.presentation.presenter.task.listeners.IOnSuccessListener;
import ru.iandreyshev.parserrss.presentation.presenter.task.InsertNewFeedTask;
import ru.iandreyshev.parserrss.presentation.view.settings.ISettingsView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class SettingsPresenter extends MvpPresenter<ISettingsView>
        implements IOnSuccessListener<IFeedInfo>, IOnErrorListener<InsertNewFeedTask.InsertError> {
    private InsertNewFeedTask insertTask;

    public void onLeaveButtonClick() {
        getViewState().openFeed();
    }

    public void insertFeed(String url) {
        if (insertTask.getStatus() == AsyncTask.Status.RUNNING) {
            return;
        } else if (insertTask.getStatus() == AsyncTask.Status.FINISHED || insertTask.isCancelled()) {
            insertTask = new InsertNewFeedTask();
        }

        insertTask
                .setSuccessListener(this)
                .setErrorListener(this)
                .execute(url);
    }

    @Override
    public void onErrorEvent(InsertNewFeedTask.InsertError insertError) {

    }

    @Override
    public void onSuccessEvent(IFeedInfo feedInfo) {

    }
}
