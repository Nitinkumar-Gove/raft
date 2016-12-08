package com.cmpe.raft.consensus.jobs;

import com.cmpe.raft.consensus.client.NodeClient;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sushant on 26-11-2016.
 */
public class HeartBeatJob {

    private String  host;
    private Integer port;
    private ScheduledFuture<?> heartBeat;

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public HeartBeatJob(String  host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public void sendHeartBeat() {
        final Runnable heartBeat = new Runnable() {
            public void run() {
                System.out.println(HeartBeatJob.class.getCanonicalName() +" Hear beat signal to "+host+":"+port);
                NodeClient client = new NodeClient(host, port);
                client.sendHeartBeat();
            }
        };

        final ScheduledFuture<?> heartBeatHandle =
                scheduler.scheduleAtFixedRate(heartBeat, 0, 5, TimeUnit.SECONDS);
        this.heartBeat = heartBeatHandle;
        scheduler.schedule(new Runnable() {
            public void run() {
                heartBeatHandle.cancel(true);
            }
        }, 365*10 , TimeUnit.DAYS);  //TODO: This should be configurable, for now running it for 10 years
    }

    public void stopJob() {
        heartBeat.cancel(true);
    }
}
