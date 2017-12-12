package ru.iandreyshev.parserrss.ui.adapter;

import android.view.View;

public interface IOnItemClickListener<TItem> {
    void onItemClick(View view, TItem item);
}
