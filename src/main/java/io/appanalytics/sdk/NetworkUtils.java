package io.appanalytics.sdk;

import android.content.Context;
import android.util.DisplayMetrics;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

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
    //private final String parameters = "?UDID=00218baddc4ef1994c66ff983b1397ca";
    private RequestQueue queue;
    private Gson gson;
    private String userAgent;

    private NetworkUtils() {
        gson = new Gson();
    }

    public void setApplicationContext(Context context) {
        queue = Volley.newRequestQueue(context);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float screenWidth  = dm.widthPixels / dm.xdpi;
        float screenHeight = dm.heightPixels / dm.ydpi;
        double size = Math.sqrt(Math.pow(screenWidth, 2) + Math.pow(screenHeight, 2));
        userAgent = size >= 7 ? "AndroidTablet" : "AndroidPhone";
    }

    public void sendDataToCloud(List<?> list, VolleyCallback callback) {
        try {
            String url = restServiceAddress + apiPathPrefix + ((Identifier) list.get(0)).getModelUrl();
            createBodyRequest(url, list, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                return body.getBytes();
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", userAgent);
                return headers;
            }
        };
        queue.add(stringRequest);
    }
}
