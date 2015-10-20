package io.appanalytics.sdk;

import java.util.Timer;

/**
 * Created by Cem Sancak on 7.10.2015.
 */
class Scheduler {

    public final static Scheduler INSTANCE = new Scheduler();
    private final int repeatEvery = 10;
    private Timer timer;

    private Scheduler() { }

    public void startScheduledTask() {
        ScheduledTask scheduledTask = new ScheduledTask();
        timer = new Timer();
        timer.schedule(scheduledTask, 0, 1000 * repeatEvery);
    }

    public void stopScheduledTask() {
        timer.cancel();
    }
}
