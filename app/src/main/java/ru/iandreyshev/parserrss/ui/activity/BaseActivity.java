package ru.iandreyshev.parserrss.ui.activity;

import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;

import ru.iandreyshev.parserrss.presentation.view.BaseView;

public abstract class BaseActivity extends MvpAppCompatActivity implements BaseView {
    @Override
    public void showShortToast(final String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLongToast(final String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
