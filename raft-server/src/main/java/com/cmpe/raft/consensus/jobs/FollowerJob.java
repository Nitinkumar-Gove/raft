package com.cmpe.raft.consensus.jobs;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sushant on 26-11-2016.
 */
public class FollowerJob {
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public void waitForHeartBeat() {
        final Runnable heartBeat = new Runnable() {
            public void run() {
                System.out.println("Waiting for hear beat signal...");
            }
        };

        final ScheduledFuture<?> heartBeatHandle =
                scheduler.scheduleAtFixedRate(heartBeat, 0, 10, TimeUnit.SECONDS);

        /*scheduler.schedule(new Runnable() {
            public void run() {
                heartBeatHandle.cancel(true);
            }
        }, 60 * 60, TimeUnit.SECONDS);*/


    }
}

