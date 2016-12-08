package com.cmpe.raft.consensus.node.state.impl;


import com.cmpe.raft.consensus.app.Application;
import com.cmpe.raft.consensus.client.NodeClient;
import com.cmpe.raft.consensus.dao.Dao;
import com.cmpe.raft.consensus.model.AddNode;
import com.cmpe.raft.consensus.model.HeartBeat;
import com.cmpe.raft.consensus.model.Person;
import com.cmpe.raft.consensus.model.Vote;
import com.cmpe.raft.consensus.node.Node;
import com.cmpe.raft.consensus.node.state.NodeState;
import com.cmpe.raft.consensus.util.ServiceUtil;
import com.cmpe.raft.consensus.util.StopWatch;
import com.sun.corba.se.spi.servicecontext.SendingContextServiceContext;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Sushant on 25-11-2016.
 */
public class Follower implements NodeState, Callable {

    private StopWatch stopWatch;
    private Node node;
    private NodeClient leaderNodeClient;

    public Follower(Node node) {
        super();
        this.node = node;
        stopWatch = new StopWatch(10000, this);
        leaderNodeClient = new NodeClient(node.getLeaderHost(), node.getLeaderPort());
    }

    @Override
    public void performTask() {
        System.out.println(Follower.class.getCanonicalName() + " STATE CHANGED");
        stopWatch.start();
        updatePersonMap();
    }

    private void updatePersonMap() {
        if (!(leaderNodeClient.getHost().isEmpty() || leaderNodeClient.getPort() == 0)) {
            leaderNodeClient.updateHost(node.getLeaderHost(), node.getLeaderPort());
            System.out.println(Follower.class.getCanonicalName() + " Getting updated person map from " + node.getLeaderHost() + ":" + node.getLeaderPort());
            List<Person> persons = leaderNodeClient.sendDoGetAllRequest();
            System.out.println(Follower.class.getCanonicalName() + " Updated with  " + persons.size());
            Dao.updateDao(persons);
        }
    }

    @Override
    public HeartBeat onHeartBeat(long term, String host, int port) {
        System.out.println(Follower.class.getCanonicalName() + " received Heart beat.");
        stopWatch.reset();
        System.out.println(Follower.class.getCanonicalName() + " " + (leaderNodeClient.getHost().isEmpty() && leaderNodeClient.getPort() == 0));

        if ((leaderNodeClient.getHost().isEmpty() && leaderNodeClient.getPort() == 0) || !(leaderNodeClient.getHost().equals(host) && leaderNodeClient.getPort().equals(port))) {
            node.setLeaderHost(host);
            node.setLeaderPort(port);
            leaderNodeClient.updateHost(node.getLeaderHost(), node.getLeaderPort());
            updatePersonMap();
        }
        if (node.getTerm() < term) {
            node.setTerm(term);
        }
        return ServiceUtil.constructHeartBeat(node);
    }

    @Override
    public Vote onCandidacyRequest(long term) {
        if (node.getTerm() < term) {
            return ServiceUtil.constructVote(node, true);     // Yes, I vote for you, as you are better leader than my current leader
        } else {
            return ServiceUtil.constructVote(node, false);    // Sorry, I'm already following someone better
        }
    }

    @Override
    public AddNode addNode(AddNode addNode) {
        Application.addNode(addNode);
        return addNode;
    }

    @Override
    public Object call() throws Exception {

        node.setCurrentState(node.getCandidateState());
        return null;
    }

    @Override
    public String getName() {
        return "FOLLOWER";
    }

    @Override
    public void stopJobs() {
        // No jobs for this state
    }
}
