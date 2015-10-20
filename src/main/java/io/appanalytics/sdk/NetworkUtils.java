package io.appanalytics.sdk;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Cem Sancak on 7.10.2015.
 */
class NetworkUtils {

    public final static NetworkUtils INSTANCE = new NetworkUtils();
    private final String restServiceAddress = "http://9ba9fbafd5a74a27b51fe0f171bfe758.cloudapp.net/";
    private final String apiPathPrefix = "api/v2/";
    //private final String parameters = "?UDID=00218baddc4ef1994c66ff983b1397ca";
    private RequestQueue queue;
    private Gson gson;

    private NetworkUtils() {
        gson = new Gson();
    }

    public void setApplicationContext(Context context) {
        queue = Volley.newRequestQueue(context);
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
        }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                String body = gson.toJson(list);
                return body.getBytes();
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        queue.add(stringRequest);
    }
}
