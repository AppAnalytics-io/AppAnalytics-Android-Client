package io.appanalytics.sdk;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import java.util.List;

/**
 * Created by Cem Sancak on 6.10.2015.
 */
class AppAnalyticsWindowCallback implements Window.Callback {
    private final int MIN_DISTANCE = 50;
    private Window.Callback windowCallback;
    private List<View> leafList;
    private int x1;
    private int y1;
    private View interacted;
    private Storage storage = Storage.INSTANCE;

    public AppAnalyticsWindowCallback(Window.Callback windowCallback, List<View> leafList) {
        this.windowCallback = windowCallback;
        this.leafList = leafList;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return windowCallback.dispatchKeyEvent(keyEvent);
    }

    @Override
    public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
        return windowCallback.dispatchKeyShortcutEvent(keyEvent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                x1 = (int) motionEvent.getRawX();
                y1 = (int) motionEvent.getRawY();
                interacted = null;
                Rect rect = new Rect();
                for (int i = leafList.size() - 1; i >= 0; --i) {
                        if (leafList.get(i).getAlpha() <= 0 || !leafList.get(i).getGlobalVisibleRect(rect)) {
                            continue;
                        }
                        if (rect.contains(x1, y1)) {
                            interacted = leafList.get(i);
                            break;
                        }
                }
                break;
            case MotionEvent.ACTION_UP:
                int x2 = (int) motionEvent.getRawX();
                int y2 = (int) motionEvent.getRawY();
                int diffX = x2 - x1;
                int diffY = y2 - y1;
                boolean xEligible = false;
                boolean yEligible = false;
                if (Math.abs(diffX) > MIN_DISTANCE) {
                    xEligible = true;
                }
                if (Math.abs(diffY) > MIN_DISTANCE) {
                    yEligible = true;
                }
                if (xEligible && yEligible) {
                    if (Math.abs(diffY) >= Math.abs(diffX)) {
                        String direction = diffY < 0 ? "up" : "down";
                        String view = interacted == null ? "Screen" : interacted.toString() + interacted.getTag();
                        Log.i("AppAnalytics", view + " swiped " + direction);
                    } else {
                        String direction = diffX < 0 ? "left" : "right";
                        String view = interacted == null ? "Screen" : interacted.toString() + interacted.getTag();
                        Log.i("AppAnalytics", view + " swiped " + direction);
                    }
                } else if (xEligible) {
                    String direction = diffX < 0 ? "left" : "right";
                    String view = interacted == null ? "Screen" : interacted.toString() + interacted.getTag();
                    Log.i("AppAnalytics", view + " swiped " + direction);
                } else if (yEligible) {
                    String direction = diffY < 0 ? "up" : "down";
                    String view = interacted == null ? "Screen" : interacted.toString() + interacted.getTag();
                    Log.i("AppAnalytics", view + " swiped " + direction);
                } else {
                    String view = interacted == null ? "Screen" : interacted.toString() + interacted.getTag();
                    Log.i("AppAnalytics", view + " clicked.");
                }
                Sample sample = new Sample(x1, x2, y1, y2);
                storage.addNewSample(sample);
                break;
        }
        return windowCallback.dispatchTouchEvent(motionEvent);
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent motionEvent) {
        return windowCallback.dispatchTrackballEvent(motionEvent);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
        return windowCallback.dispatchGenericMotionEvent(motionEvent);
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        return windowCallback.dispatchPopulateAccessibilityEvent(accessibilityEvent);
    }

    @Override
    public View onCreatePanelView(int i) {
        return windowCallback.onCreatePanelView(i);
    }

    @Override
    public boolean onCreatePanelMenu(int i, Menu menu) {
        return windowCallback.onCreatePanelMenu(i, menu);
    }

    @Override
    public boolean onPreparePanel(int i, View view, Menu menu) {
        return windowCallback.onPreparePanel(i, view, menu);
    }

    @Override
    public boolean onMenuOpened(int i, Menu menu) {
        return windowCallback.onMenuOpened(i, menu);
    }

    @Override
    public boolean onMenuItemSelected(int i, MenuItem menuItem) {
        return windowCallback.onMenuItemSelected(i, menuItem);
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams layoutParams) {
        windowCallback.onWindowAttributesChanged(layoutParams);
    }

    @Override
    public void onContentChanged() {
        windowCallback.onContentChanged();
    }

    @Override
    public void onWindowFocusChanged(boolean b) {
        windowCallback.onWindowFocusChanged(b);
    }

    @Override
    public void onAttachedToWindow() {
        windowCallback.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        windowCallback.onDetachedFromWindow();
    }

    @Override
    public void onPanelClosed(int i, Menu menu) {
        windowCallback.onPanelClosed(i, menu);
    }

    @Override
    public boolean onSearchRequested() {
        return windowCallback.onSearchRequested();
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onSearchRequested(SearchEvent searchEvent) {
        return windowCallback.onSearchRequested(searchEvent);
    }

    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return windowCallback.onWindowStartingActionMode(callback);
    }

    @SuppressLint("NewApi")
    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int i) {
        return windowCallback.onWindowStartingActionMode(callback, i);
    }

    @Override
    public void onActionModeStarted(ActionMode actionMode) {
        windowCallback.onActionModeStarted(actionMode);
    }

    @Override
    public void onActionModeFinished(ActionMode actionMode) {
        windowCallback.onActionModeFinished(actionMode);
    }
}
