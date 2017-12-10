package ru.iandreyshev.parserrss.ui.activity;

import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;

import ru.iandreyshev.parserrss.presentation.view.IBaseView;

abstract class BaseActivity extends MvpAppCompatActivity implements IBaseView {
    @Override
    public final void showShortToast(final String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public final void showLongToast(final String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
