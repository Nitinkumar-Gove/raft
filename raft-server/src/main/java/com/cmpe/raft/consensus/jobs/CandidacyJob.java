package com.cmpe.raft.consensus.jobs;

import com.cmpe.raft.consensus.client.NodeClient;
import com.cmpe.raft.consensus.model.Vote;
import com.cmpe.raft.consensus.node.state.impl.Candidate;

import java.util.concurrent.*;

/**
 * Created by Sushant on 27-11-2016.
 */
public class CandidacyJob {
    private Candidate candidate;
    private String host;
    private Integer port;

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(20);

    public CandidacyJob(Candidate candidate, String host, Integer port) {
        this.candidate = candidate;
        this.host = host;
        this.port = port;
    }

    public void sendCandidacyRequest() {
        final Thread voteThread = new Thread() {
            public void run() {
                System.out.println(CandidacyJob.class.getCanonicalName()+"Candidacy request signal...");
                NodeClient client = new NodeClient(host, port);
                Vote vote = client.sendCandidacyRequest();
                candidate.onResponse(vote);
            }
        };
        voteThread.start();
    }
}
