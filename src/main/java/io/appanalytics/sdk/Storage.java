package io.appanalytics.sdk;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Cem Sancak on 7.10.2015.
 */
class Storage {

    public final static Storage INSTANCE = new Storage();
    private List<Manifest> manifestList;
    private List<Sample> sampleList;
    private List<Activity> openedActivities;
    private String sessionID;
    private int actionOrder;

    public String getAndroidID() {
        return androidID;
    }

    public void setAndroidID(String androidID) {
        this.androidID = androidID;
    }

    private String androidID;
    private StorageManager storageManager = StorageManager.INSTANCE;

    private Storage() {
        openedActivities = new ArrayList<>();
        sampleList = storageManager.loadData(Sample.class);
        manifestList = storageManager.loadData(Manifest.class);
    }

    public void resetActionOrder() {
        actionOrder = 1;
    }

    public void incrementActionOrder() {
        ++actionOrder;
    }

    public int getActionOrder() {
        return actionOrder;
    }

    public void setSessionID(UUID sessionID) {
        this.sessionID = sessionID.toString();
    }

    public String getSessionID() {
        return sessionID;
    }

    public void saveToStorage() {
        if (sampleList.size() > 0) {
            storageManager.saveData(sampleList);
        }
        if (manifestList.size() > 0) {
            storageManager.saveData(manifestList);
        }
    }

    public void clearSamples() {
        sampleList.clear();
    }

    public void clearManifests() {
        manifestList.clear();
    }

    public List<Sample> getSamples() {
        return sampleList;
    }

    public void addNewSample(Sample sample) {
        sampleList.add(sample);
    }

    public void addNewManifest(Manifest manifest) {
        manifestList.add(manifest);
    }

    public List<Manifest> getManifests() {
        return manifestList;
    }

    public boolean anyOpenActivities() {
        return openedActivities.size() > 0;
    }

    public void addActivity(Activity activity) {
        openedActivities.add(activity);
    }

    public void removeActivity(Activity activity) {
        openedActivities.remove(activity);
    }
}
