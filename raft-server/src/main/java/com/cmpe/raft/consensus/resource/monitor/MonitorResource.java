package com.cmpe.raft.consensus.resource.monitor;

import com.cmpe.raft.consensus.model.ServerStatus;
import com.cmpe.raft.consensus.model.Status;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 * Created by Sushant on 07-12-2016.
 */
@Path("/monitor")
public class MonitorResource {

    private static String callUri = "http://%s:%d/raft/node/";
    private static Map<String, Set<Integer>> clusterNodes = new HashMap<>();
    private static final String HM_NAME = "node";
    private static final Jedis JEDIS = new Jedis("localhost");

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNodeStatus() {
        List<ServerStatus> statuses = new ArrayList<>();
        updateClusterNodes();
        for (String host: clusterNodes.keySet()) {
            for (Integer port: clusterNodes.get(host)) {
                String status = getStatus(host, port);
                ServerStatus serverStatus = new ServerStatus(host, port, status);
                statuses.add(serverStatus);
            }
        }
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity(new Gson().toJson(statuses))
                .build();
    }

    public String getStatus(String host, int port) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(String.format(callUri, host, port));
        Response response = target
                .request(MediaType.APPLICATION_JSON)
                .get();
        Status status = response.readEntity(Status.class);
        //Test if not null
        return status.getName();
    }

    public static void updateClusterNodes() {
        Map<String, String> nodeMap = JEDIS.hgetAll(HM_NAME);
        Set<String> hosts = nodeMap.keySet();
        for (String host: hosts) {
            String ports = JEDIS.hget(HM_NAME, host);
            Set<Integer> portSet = new HashSet<>();
            for (String addPort: ports.split(",")) {
                portSet.add(Integer.parseInt(addPort));
                System.out.println("Adding to cluster node: "+ host+":"+addPort);
            }
            clusterNodes.put(host, portSet);
        }
    }

    public static void main(String[] args) {
        MonitorResource monitorResource = new MonitorResource();
        updateClusterNodes();
        for (String host: clusterNodes.keySet()) {
            for (Integer port: clusterNodes.get(host)) {
                String status = monitorResource.getStatus(host, port);
                ServerStatus serverStatus = new ServerStatus(host, port, status);
                System.out.println(host + " "+ port+ " "+ serverStatus.getStatus());
            }
        }
    }
}
