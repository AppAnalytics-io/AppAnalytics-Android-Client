package io.appanalytics.sdk;

import android.app.Application;
import android.util.Log;

/**
 * Created by Cem Sancak on 7.10.2015.
 */
public class AppAnalytics {

    public AppAnalytics(Application application, String apiKey) {
        Log.i("AppAnalytics", "Hooked");
        AppAnalyticsActivityCallback aaac = new AppAnalyticsActivityCallback(apiKey, application.getApplicationContext());
        application.registerActivityLifecycleCallbacks(aaac);
    }

}
