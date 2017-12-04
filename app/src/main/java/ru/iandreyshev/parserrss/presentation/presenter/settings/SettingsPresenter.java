package ru.iandreyshev.parserrss.presentation.presenter.settings;


import ru.iandreyshev.parserrss.presentation.view.settings.SettingsView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class SettingsPresenter extends MvpPresenter<SettingsView> {
    public void onLeaveButtonClick() {
        getViewState().openFeed();
    }

    public void addNewFeed(String url) {
    }
}
