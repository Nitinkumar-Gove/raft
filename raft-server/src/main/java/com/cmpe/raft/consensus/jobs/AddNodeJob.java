package com.cmpe.raft.consensus.jobs;

import com.cmpe.raft.consensus.client.NodeClient;
import com.cmpe.raft.consensus.model.AddNode;

/**
 * Created by Sushant on 30-11-2016.
 */
public class AddNodeJob {
    private String host;
    private Integer port;

    public AddNodeJob(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public void sendAddNodeRequest() {
        final Thread addNodeThread = new Thread() {
            public void run() {
                System.out.println(AddNodeJob.class.getCanonicalName()+" add node request signal to "+ host+":"+port);
                NodeClient client = new NodeClient(host, port);
                AddNode addNode = client.sendAddNodeRequest();
                System.out.println(AddNodeJob.class.getCanonicalName()+" Received add node response from "+ host+":"+port +" response "+ addNode);
            }
        };
        addNodeThread.start();
    }
}
