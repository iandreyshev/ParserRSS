package ru.iandreyshev.parserrss.presentation.presenter.settings;


import ru.iandreyshev.parserrss.presentation.view.settings.ISettingsView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class SettingsPresenter extends MvpPresenter<ISettingsView> {
    public void onLeaveButtonClick() {
        getViewState().openFeed();
    }

    public void addNewFeed(String url) {
    }
}
