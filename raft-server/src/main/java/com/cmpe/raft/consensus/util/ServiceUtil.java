package com.cmpe.raft.consensus.util;

import com.cmpe.raft.consensus.app.Application;
import com.cmpe.raft.consensus.model.HeartBeat;
import com.cmpe.raft.consensus.model.Vote;
import com.cmpe.raft.consensus.node.Node;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Sushant on 27-11-2016.
 */
public class ServiceUtil {

    public static HeartBeat constructHeartBeat(Node node) {
        HeartBeat heartBeat = new HeartBeat();
        heartBeat.setDate(new Date());
        heartBeat.setIp(Application.getIp());
        heartBeat.setPort(Application.getPort());
        heartBeat.setTerm(node.getTerm());
        return heartBeat;
    }

    public static Vote constructVote(Node node, boolean accept) {
        Vote vote = new Vote();
        vote.setPort(Application.getPort());
        vote.setIp(Application.getIp());
        vote.setDate(new Date());
        vote.setVote(accept);
        return vote;

    }
}
