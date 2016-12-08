package com.cmpe.raft.consensus.util;

import java.util.concurrent.Callable;

/**
 * Created by Sushant on 26-11-2016.
 */
public class StopWatch extends Thread
{
    private long startTime;
    private long timeElaspsed;
    private long stopTime;
    private Callable callback;

    public StopWatch(long stopTime, Callable callback) {
        this.stopTime = stopTime;
        this.callback = callback;
    }

    @Override
    public void run() {
        System.out.println(StopWatch.class.getCanonicalName() + " Stop watch started");
        startTime = System.currentTimeMillis();
        while(timeElaspsed < stopTime) {
            timeElaspsed = System.currentTimeMillis() - startTime;
        }
        try {
            System.out.println(StopWatch.class.getCanonicalName() + " Stop watch callback");
            callback.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        System.out.println(StopWatch.class.getCanonicalName() + " Stop watch reset");
        startTime = System.currentTimeMillis();
    }

    public long getTimeElapsed() {
        return timeElaspsed;
    }
}
