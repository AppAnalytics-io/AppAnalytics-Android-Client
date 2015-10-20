package io.appanalytics.sdk;

import java.io.Serializable;

/**
 * Created by Cem Sancak on 7.10.2015.
 */
class Sample implements Identifier, Serializable {

    private float x1;
    private float y1;
    private float x2;
    private float y2;

    public Sample(float x1, float x2, float y1, float y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    @Override
    public String getModelUrl() {
        return "samples";
    }
}
