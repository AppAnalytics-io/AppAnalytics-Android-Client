package io.appanalytics.sdk;

import android.app.Application;
import android.util.Log;

/**
 * Created by Cem Sancak on 7.10.2015.
 */
public class AppAnalytics {

    public AppAnalytics(Application application, String apiKey) {

        //Storage.INSTANCE.setApiKey(apiKey);

        //StorageManager.INSTANCE.setContext(application.getApplicationContext());

        //Storage.INSTANCE.initializeStorage();

        //StorageManager.INSTANCE.initializeStorageManager();
        Log.i("AppAnalytics", "Hooked");
        AppAnalyticsActivityCallback aaac = new AppAnalyticsActivityCallback(apiKey, application.getApplicationContext());
        application.registerActivityLifecycleCallbacks(aaac);
    }

}
