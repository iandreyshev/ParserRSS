package ru.iandreyshev.parserrss.presentation.presenter;

import android.os.AsyncTask;

import ru.iandreyshev.parserrss.models.feed.IFeedInfo;
import ru.iandreyshev.parserrss.presentation.presenter.task.listeners.IOnErrorListener;
import ru.iandreyshev.parserrss.presentation.presenter.task.listeners.IOnSuccessListener;
import ru.iandreyshev.parserrss.presentation.presenter.task.InsertFeedTask;
import ru.iandreyshev.parserrss.presentation.view.ISettingsView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class SettingsPresenter extends MvpPresenter<ISettingsView>
        implements IOnSuccessListener<IFeedInfo>, IOnErrorListener<InsertFeedTask.Status> {
    private InsertFeedTask mInsertTask;

    public void onLeaveButtonClick() {
        getViewState().openFeed();
    }

    public void insertFeed(String url) {
        if (mInsertTask.getStatus() == AsyncTask.Status.RUNNING) {
            return;
        } else if (mInsertTask.getStatus() == AsyncTask.Status.FINISHED || mInsertTask.isCancelled()) {
            mInsertTask = new InsertFeedTask();
        }

        mInsertTask
                .setSuccessListener(this)
                .setErrorListener(this)
                .execute(url);
    }

    @Override
    public void onErrorEvent(InsertFeedTask.Status insertError) {

    }

    @Override
    public void onSuccessEvent(IFeedInfo feedInfo) {

    }
}
