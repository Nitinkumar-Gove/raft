package com.cmpe.raft.consensus.node.state.impl;

import com.cmpe.raft.consensus.app.Application;
import com.cmpe.raft.consensus.jobs.HeartBeatJob;
import com.cmpe.raft.consensus.jobs.WorkJob;
import com.cmpe.raft.consensus.model.AddNode;
import com.cmpe.raft.consensus.model.HeartBeat;
import com.cmpe.raft.consensus.model.Vote;
import com.cmpe.raft.consensus.node.Node;
import com.cmpe.raft.consensus.node.state.NodeState;
import com.cmpe.raft.consensus.util.ServiceUtil;
import com.sun.jersey.core.impl.provider.entity.XMLJAXBElementProvider;
import sun.applet.AppletListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sushant on 25-11-2016.
 */
public class Leader implements NodeState {
    private Node node;
    private static List<HeartBeatJob> heartBeatJobs = new ArrayList<>();
    private static WorkJob workJob = new WorkJob();

    public Leader(Node node) {
        super();
        this.node = node;
    }

    @Override
    public void performTask() {
        System.out.println(Leader.class.getCanonicalName() + " STATE CHANGED");
        node.setLeaderHost(Application.getIp());
        node.setLeaderPort(Application.getPort());
        //yay, I've to keep sending heart beat to all
        for (String host : Application.getClusterNodes().keySet()) {
            for (Integer port : Application.getClusterNodes().get(host)) {
                if (!(Application.getIp().equals(host) && Application.getPort().equals(port))) {
                    HeartBeatJob heartBeatJob = new HeartBeatJob(host, port);
                    heartBeatJob.sendHeartBeat();
                    heartBeatJobs.add(heartBeatJob);
                }
            }
        }
        workJob.listen();
    }

    @Override
    public HeartBeat onHeartBeat(long term, String host, int port) {
        System.out.println(Leader.class.getCanonicalName() + " received Heart beat.");
        if (node.getTerm() < term) {
            //I resign from my post as a Leader as you are better than me and I will follow your footsteps
            node.setTerm(term);
            node.setLeaderHost(host);
            node.setLeaderPort(port);
            node.setCurrentState(node.getFollowerState());
        }
        return ServiceUtil.constructHeartBeat(node);
    }

    @Override
    public Vote onCandidacyRequest(long term) {
        return ServiceUtil.constructVote(node, false);    // You wish ! I'm not dead yet
    }

    @Override
    public AddNode addNode(AddNode addNode) {
        System.out.println(Leader.class.getCanonicalName() + " adding node "+ addNode.getIp()+":"+ addNode.getPort());
        Application.addNode(addNode);
        new HeartBeatJob(addNode.getIp(), addNode.getPort()).sendHeartBeat();
        return addNode;
    }

    @Override
    public String getName() {
        return "LEADER";
    }

    @Override
    public void stopJobs() {
        for (HeartBeatJob heartBeatJob: heartBeatJobs) {
            heartBeatJob.stopJob();
        }
        workJob.stopListeing();
    }
}
