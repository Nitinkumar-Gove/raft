package com.cmpe.raft.consensus.node.state.impl;


import com.cmpe.raft.consensus.app.Application;
import com.cmpe.raft.consensus.jobs.CandidacyJob;
import com.cmpe.raft.consensus.model.AddNode;
import com.cmpe.raft.consensus.model.HeartBeat;
import com.cmpe.raft.consensus.model.Vote;
import com.cmpe.raft.consensus.node.Node;
import com.cmpe.raft.consensus.node.state.NodeState;
import com.cmpe.raft.consensus.util.ServiceUtil;

/**
 * Created by Sushant on 25-11-2016.
 */
public class Candidate implements NodeState {

    private Node node;
    private Integer numberOfAccepts;
    private Integer numberOfResponses;
    private Integer numberOfRejects;

    public Candidate(Node node) {
        super();
        this.node = node;
        numberOfAccepts = 0;
        numberOfResponses = 0;
        numberOfRejects = 0;
    }

    @Override
    public void performTask() {
        System.out.println(Candidate.class.getCanonicalName() + " STATE CHANGED");
        //Every time state changes to candidate, reset vote count to 1 ny voting itself
        numberOfAccepts = 1;
        numberOfResponses = 0;
        numberOfRejects = 0;
        node.setTerm(node.getTerm() + 1); //Increment term

        System.out.println(Candidate.class.getCanonicalName()+" number of nodes "+ Application.getNumberOfNodes());
        if (Application.getNumberOfNodes() > 1) {
            //send candidacy request to all the nodes in the cluster and wait till it gets maximum votes
            for (String host : Application.getClusterNodes().keySet()) {
                for (Integer port : Application.getClusterNodes().get(host)) {
                    if (!(host.equals(Application.getIp()) && port.equals(Application.getPort()))) {
                        new CandidacyJob(this, host, port).sendCandidacyRequest();
                    }
                }
            }
        } else {    //you are the only candidate in cluster be a leader instead
            node.setCurrentState(node.getLeaderState());
        }
    }

    @Override
    public HeartBeat onHeartBeat(long term, String host, int port) {
        System.out.println(Candidate.class.getCanonicalName() + " received Heart beat.");
        if (node.getTerm() < term) {
            node.setTerm(term);
            node.setLeaderHost(host);
            node.setLeaderPort(port);
            node.setCurrentState(node.getFollowerState()); //finds a better candidate
        }
        return ServiceUtil.constructHeartBeat(node);
    }

    @Override
    public Vote onCandidacyRequest(long term) {
        return ServiceUtil.constructVote(node, false);  //I'm a candidate too, so sorry
    }

    @Override
    public AddNode addNode(AddNode addNode) {
        Application.addNode(addNode);
        new CandidacyJob(this, addNode.getIp(), addNode.getPort()).sendCandidacyRequest();
        return addNode;
    }

    @Override
    public String getName() {
        return "CANDIDATE";
    }

    @Override
    public void stopJobs() {
        // No jobs running for this state
    }

    public void onResponse(Vote vote) {
        numberOfResponses++;
        if (vote != null && vote.getVote()) {
            numberOfAccepts++;
        } else if (vote != null) {
            numberOfRejects++;
        }

        //State check to make sure that you are still a candidate
        //can safely use 'this' for comparision as there will only be an instance for each state
        if (numberOfAccepts > Application.getNumberOfNodes() % 2
                && node.getCurrentState().equals(this)) {
            node.setCurrentState(node.getLeaderState());
        }
        //And finally check if you have received all the responses but not been voted, become follower
        else if ((numberOfResponses == Application.getNumberOfNodes() - 1) && node.getCurrentState().equals(this)) {
            if (numberOfAccepts > numberOfRejects) {
                node.setCurrentState(node.getLeaderState());  //Received all the responses but maybe few nodes are down
            } else {
                node.setCurrentState(node.getFollowerState());
            }
        }
    }


}