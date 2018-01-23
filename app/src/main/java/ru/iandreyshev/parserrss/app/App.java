package ru.iandreyshev.parserrss.app;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

import io.objectbox.BoxStore;
import ru.iandreyshev.parserrss.models.repository.Database;
import ru.iandreyshev.parserrss.models.repository.MyObjectBox;

public class App extends Application {
    private static WeakReference<Context> mContext;
    private static BoxStore mBoxStore;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = new WeakReference<>(App.this);
        mBoxStore = MyObjectBox.builder().androidContext(App.this).build();
    }

    @NonNull
    public static Database getDatabase() {
        return new Database(mBoxStore);
    }

    public static String getStr(int id) {
        return mContext.get().getString(id);
    }
}
