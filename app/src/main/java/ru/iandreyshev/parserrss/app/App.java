package ru.iandreyshev.parserrss.app;

import android.app.Application;
import android.util.Log;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;
import io.objectbox.android.BuildConfig;
import ru.iandreyshev.parserrss.models.repository.Database;
import ru.iandreyshev.parserrss.models.repository.MyObjectBox;

public class App extends Application {
    private static final String TAG = App.class.getName();
    private static final String START_MESSAGE_PATTERN = "Using ObjectBox %s (%s)";

    private static App mInstance;
    private static BoxStore mBoxStore;

    public App() {
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mBoxStore = MyObjectBox.builder().androidContext(App.this).build();

        if (BuildConfig.DEBUG) {
            new AndroidObjectBrowser(mBoxStore).start(this);
        }

        Log.d(TAG, String.format(START_MESSAGE_PATTERN, BoxStore.getVersion(), BoxStore.getVersionNative()));
    }

    public static String getStr(int id) {
        return mInstance.getString(id);
    }

    public static Database getDatabase() {
        return new Database(mBoxStore);
    }
}
