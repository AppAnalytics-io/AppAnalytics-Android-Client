package io.appanalytics.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Cem Sancak on 6.10.2015.
 */
class AppAnalyticsActivityCallback implements Application.ActivityLifecycleCallbacks {

    private Storage storage;
    private Scheduler scheduler = Scheduler.INSTANCE;
    private String apiKey;
    private Locale locale;
    private String appVersion;
    private int resX;
    private int resY;
    private String userAgent;

    public AppAnalyticsActivityCallback(String apiKey, Context context) {
        this.apiKey = apiKey;
        locale = context.getResources().getConfiguration().locale;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AppAnalytics", "Package name not found");
        }
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        resX = dm.widthPixels;
        resY = dm.heightPixels;
        float screenWidth  = resX / dm.xdpi;
        float screenHeight = resY / dm.ydpi;
        double size = Math.sqrt(Math.pow(screenWidth, 2) + Math.pow(screenHeight, 2));
        userAgent = size >= 6.5 ? "AndroidTablet/69" : "AndroidPhone/69";
        StorageManager.INSTANCE.initializeStorageManager(apiKey, context);
        NetworkUtils.INSTANCE.setApplicationContext(context);
        storage = Storage.INSTANCE;
        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        storage.setAndroidID(androidID);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) { }

    @Override
    public void onActivityStarted(Activity activity) {

        Window window = activity.getWindow();
        Window.Callback callback = window.getCallback();

        if (!(callback instanceof AppAnalyticsWindowCallback)) {
            final List<View> leafList = new ArrayList<>();
            final ViewGroup decorView = (ViewGroup) window.getDecorView();
            decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getLeafViews(decorView, leafList);
                }
            });
            AppAnalyticsWindowCallback aawc = new AppAnalyticsWindowCallback(callback, leafList, activity);
            window.setCallback(aawc);
        }

        if (!storage.anyOpenActivities()) {
            storage.setSessionID(UUID.randomUUID());
            storage.resetActionOrder();
            Manifest manifest = new Manifest(apiKey, storage.getAndroidID(), resX, resY, System.currentTimeMillis(), storage.getSessionID(), locale.getCountry(), Build.VERSION.RELEASE, appVersion, userAgent);
            storage.addNewManifest(manifest);
            scheduler.startScheduledTask();
        }

        storage.addActivity(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) { }

    @Override
    public void onActivityPaused(Activity activity) { }

    @Override
    public void onActivityStopped(Activity activity) {
        storage.removeActivity(activity);
        if (!storage.anyOpenActivities()) {
            scheduler.stopScheduledTask();
            storage.saveToStorage();
            Log.i("AppAnalyticsStorage", "List saved: " + storage.getSamples().size());
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) { }

    @Override
    public void onActivityDestroyed(Activity activity) { }

    private void getLeafViews(View decorView, List<View> leafList) {
        leafList.clear();
        List<View> unvisited = new ArrayList<>();
        unvisited.add(decorView);
        while (!unvisited.isEmpty()) {
            View child = unvisited.remove(0);
            if (child.getVisibility() != View.VISIBLE) {
                continue;
            }
            if (!(child instanceof ViewGroup)) {
                leafList.add(child);
                continue;
            }
            ViewGroup group = (ViewGroup) child;
            for (int i = 0; i < group.getChildCount(); ++i) {
                unvisited.add(group.getChildAt(i));
            }
        }
    }
}
