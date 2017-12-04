package ru.iandreyshev.parserrss.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

public class App extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Resources getRes() {
        return context.getResources();
    }
}
