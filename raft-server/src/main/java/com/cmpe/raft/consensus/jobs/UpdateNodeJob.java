package com.cmpe.raft.consensus.jobs;

import com.cmpe.raft.consensus.client.NodeClient;
import com.cmpe.raft.consensus.model.ClientRequest;

/**
 * Created by Sushant on 06-12-2016.
 */
public class UpdateNodeJob {
    private ClientRequest clientRequest;
    private NodeClient nodeClient;

    public UpdateNodeJob(ClientRequest clientRequest, NodeClient nodeClient) {
        this.clientRequest = clientRequest;
        this.nodeClient = nodeClient;
    }

    public void doAction() {
        final Thread updateThread = new Thread() {
            public void run() {
                switch (clientRequest.getAction()) {
                    case "POST":
                        System.out.println(WorkJob.class.getCanonicalName() +" Posting new person to " + nodeClient.getHost()+":"+nodeClient.getPort());
                        nodeClient.sendDoPostRequest(clientRequest.getPerson());
                        break;
                    case "PUT":
                        nodeClient.sendDoPutRequest(clientRequest.getPerson());
                        break;
                    case "DELETE":
                        nodeClient.sendDoDeleteRequest(clientRequest.getPerson().getId());
                        break;
                }
            }
        };
        updateThread.start();
    }
}
