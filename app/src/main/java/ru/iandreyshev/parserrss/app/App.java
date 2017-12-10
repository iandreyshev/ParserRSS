package ru.iandreyshev.parserrss.app;

import android.app.Application;
import android.util.Log;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;
import io.objectbox.android.BuildConfig;
import ru.iandreyshev.parserrss.models.rss.MyObjectBox;

public class App extends Application {
    public static final String TAG = App.class.getName();
    public static final String START_MESSAGE_PATTERN = "Using ObjectBox %s (%s)";

    private static BoxStore mBoxStore;

    @Override
    public void onCreate() {
        super.onCreate();

        mBoxStore = MyObjectBox.builder().androidContext(App.this).build();

        if (BuildConfig.DEBUG) {
            new AndroidObjectBrowser(mBoxStore).start(this);
        }

        Log.d(TAG, String.format(START_MESSAGE_PATTERN, BoxStore.getVersion(), BoxStore.getVersionNative()));
    }

    public static BoxStore getBoxStore() {
        return mBoxStore;
    }
}
