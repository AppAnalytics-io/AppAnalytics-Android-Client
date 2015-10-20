package io.appanalytics.sdk;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Cem Sancak on 7.10.2015.
 */
class Manifest implements Identifier, Serializable {

    private String apiKey;
    private String AID;
    private int resX;
    private int resY;
    private long startDate;
    private UUID sessionID;
    private String locale;
    private String versionOs;
    private String versionApp;

    public Manifest() { }

    public Manifest(String apiKey, String AID, int resX, int resY, long startDate, UUID sessionID, String locale, String versionOs, String versionApp) {
        this.apiKey = apiKey;
        this.AID = AID;
        this.resX = resX;
        this.resY = resY;
        this.startDate = startDate;
        this.sessionID = sessionID;
        this.locale = locale;
        this.versionOs = versionOs;
        this.versionApp = versionApp;
    }

    @Override
    public String getModelUrl() {
        return "androidManifest";
    }

}
