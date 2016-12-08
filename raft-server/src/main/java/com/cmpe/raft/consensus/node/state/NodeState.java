package com.cmpe.raft.consensus.node.state;

import com.cmpe.raft.consensus.model.AddNode;
import com.cmpe.raft.consensus.model.HeartBeat;
import com.cmpe.raft.consensus.model.Vote;

/**
 * Created by Sushant on 25-11-2016.
 */
public interface NodeState {

    void performTask();

    HeartBeat onHeartBeat(long term, String host, int port);

    Vote onCandidacyRequest(long term);

    AddNode addNode(AddNode addNode);

    String getName();

    void stopJobs();

}
