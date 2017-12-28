package ru.iandreyshev.parserrss.ui.fragment;

import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;

import ru.iandreyshev.parserrss.presentation.view.IBaseView;

abstract class BaseFragment extends MvpAppCompatFragment implements IBaseView {
    @Override
    public void showShortToast(final String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLongToast(final String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
