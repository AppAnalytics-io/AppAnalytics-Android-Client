package io.appanalytics.sdk;

import com.android.volley.VolleyError;

/**
 * Created by Cem Sancak on 7.10.2015.
 */
interface VolleyCallback {
    void onSuccess(String result);
    void onError(VolleyError error);
}
