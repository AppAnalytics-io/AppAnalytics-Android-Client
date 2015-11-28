package io.appanalytics.sdk;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Cem Sancak on 7.10.2015.
 */
class NetworkUtils {

    public final static NetworkUtils INSTANCE = new NetworkUtils();
    private final String restServiceAddress = "https://gerison.appanalytics.io/";
    private final String apiPathPrefix = "api/v2/";
    private RequestQueue queue;
    private Gson gson;

    private NetworkUtils() {
        gson = new Gson();
    }

    public void setApplicationContext(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public void sendDataToCloud(List<?> list, VolleyCallback callback) {
        String url = restServiceAddress + apiPathPrefix + ((Identifier) list.get(0)).getModelUrl();
        createBodyRequest(url, list, callback);
    }

    private void createBodyRequest(String url, final List<?> list, final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        })  {
            @Override
            public byte[] getBody() throws AuthFailureError {
                String body = gson.toJson(list);
                //Log.i("gsoned", body);
                return body.getBytes();
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("apiSecret", "VWZVqqrDjs7262shGhwKzb69hGG34BT99vUgv98Ac338fZGOo4");
                return headers;
            }
        };
        queue.add(stringRequest);
    }
}
