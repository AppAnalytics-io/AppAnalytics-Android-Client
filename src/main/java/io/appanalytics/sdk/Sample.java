package io.appanalytics.sdk;

import java.io.Serializable;

/**
 * Created by Cem Sancak on 7.10.2015.
 */
class Sample implements Identifier, Serializable {
    private int actionId;
    private float x;
    private float y;
    private String elementId;
    private int actionOrder;
    private String url;
    private String sessionID;
    private long timestamp;
    private String androidID;

    public Sample(float x, float y, long timestamp, String elementId, String url, String sessionID, String androidID) {
        this.x = x;
        this.y = y;
        actionId = 1;
        this.timestamp = timestamp;
        this.elementId = elementId;
        this.url = url;
        this.sessionID = sessionID;
        this.androidID = androidID;
    }

    @Override
    public String getModelUrl() {
        return "samples";
    }
}
