package io.appanalytics.sdk;

import android.util.Log;

import com.android.volley.VolleyError;

import java.util.List;
import java.util.TimerTask;

/**
 * Created by Cem Sancak on 7.10.2015.
 */
class ScheduledTask extends TimerTask {

    private NetworkUtils networkUtils;
    private Storage storage;

    public ScheduledTask() {
        networkUtils = NetworkUtils.INSTANCE;
        storage = Storage.INSTANCE;
    }

    @Override
    public void run() {
        //List<Sample> sampleList = storage.getSamples();
        List<Manifest> sampleList = storage.getManifests();
        if (sampleList.size() > 0) {
            networkUtils.sendDataToCloud(sampleList, new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.i("AppAnalytics", result);
                    storage.clearSamples();
                }
                @Override
                public void onError(VolleyError result) {
                    Log.e("AppAnalytics", result.getClass().getSimpleName());
                }
            });
        }
    }
}
