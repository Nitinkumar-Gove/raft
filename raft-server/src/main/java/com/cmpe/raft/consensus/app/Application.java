package com.cmpe.raft.consensus.app;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.cmpe.raft.consensus.jobs.AddNodeJob;
import com.cmpe.raft.consensus.model.AddNode;
import com.cmpe.raft.consensus.node.Node;
import org.glassfish.grizzly.PortRange;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sushant on 26-11-2016.
 */
public class Application {

    @Parameter(names = {"--ipaddress", "-ip"}, description = "IP address of the node")
    private static String ip = "localhost";
    @Parameter(names = {"--port", "-p"}, description = "Port of the node")
    private static Integer port = 8080;
    @Parameter(names = {"--dockerised", "-do"}, description = "Is the app dockerised")
    private static boolean dockerised = false;
    private static String uri = "http://%s:%d/raft/";
    private static Map<String, Set<Integer>> clusterNodes = new HashMap<>();
    private static final String HM_NAME = "node";
    private static final Jedis JEDIS = new Jedis("localhost");
    private static boolean alreadyAdded = false;

    private static HttpServer startServer() {
        updateClusterNodes();
        //Send reactToNode request to all in the cluster if you are not in Redis already
        if (!alreadyAdded) {
            System.out.println(Application.class.getCanonicalName() + " " + ip + ":" + port + " is not present");
            for (String host : Application.getClusterNodes().keySet()) {
                for (Integer clusterPort : Application.getClusterNodes().get(host)) {
                    if (!(host.equals(ip) && clusterPort.equals(port))) {
                        System.out.println(Application.class.getCanonicalName() + " creating new add node Job for "
                                + host + ":" + clusterPort + " from " + ip + ":" + port);
                        new AddNodeJob(host, clusterPort).sendAddNodeRequest();
                    }
                }
            }
        }

        uri = String.format(uri, ip, port);
        System.out.println(uri);
        if (!dockerised) {
            final ResourceConfig rc = new ResourceConfig().packages("com.cmpe.raft.consensus");
            return GrizzlyHttpServerFactory.createHttpServer(URI.create(uri), rc);
        } else {
            HttpServer server = new HttpServer();
            NetworkListener listener = new NetworkListener("grizzly", ip, new PortRange(port));
            server.addListener(listener);
            return server;
        }
    }

    public static void updateClusterNodes() {
        Map<String, String> nodeMap = JEDIS.hgetAll(HM_NAME);
        Set<String> hosts = nodeMap.keySet();
        if (hosts.contains(ip)) {
            String ports = JEDIS.hget(HM_NAME, ip);
            if (!ports.contains(Integer.toString(port))) {
                System.out.println(Application.class.getCanonicalName() + " hget : " + ports);
                ports = String.format(ports + ",%d", port);
                System.out.println(Application.class.getCanonicalName() + " updated ports : " + ports);
                nodeMap.put(ip, ports);
            } else {
                alreadyAdded = true;
            }
        } else {
            System.out.println(Application.class.getCanonicalName() + " added port : " + port);
            nodeMap.put(ip, Integer.toString(port));
        }
        String response = JEDIS.hmset(HM_NAME, nodeMap);
        System.out.println(Application.class.getCanonicalName() + " response : " + response);
        for (String host : nodeMap.keySet()) {
            Set<Integer> portList = new HashSet<>();
            String[] portsString = nodeMap.get(host).split(",");
            for (String portString : portsString) {
                portList.add(Integer.parseInt(portString));
            }
            clusterNodes.put(host, portList);
        }
    }

    private static void deleteNode() {
        String ports = JEDIS.hget(HM_NAME, ip);
        String[] portsString = ports.split(",");
        if (portsString.length == 1) {
            JEDIS.hdel(HM_NAME, ip);
        } else {
            if (ports.contains("," + port)) {
                ports = ports.replace("," + port, "");
            } else {
                ports = ports.replace(port + ",", "");
            }
            Map<String, String> update = new HashMap<>();
            update.put(ip, ports);
            JEDIS.hmset(HM_NAME, update);
        }
    }

    public static String getIp() {
        return ip;
    }

    public static Integer getPort() {
        return port;
    }

    public static Map<String, Set<Integer>> getClusterNodes() {
        return clusterNodes;
    }

    public static Integer getNumberOfNodes() {
        int i = 0;
        for (String host: clusterNodes.keySet()) {
            i += clusterNodes.get(host).size();
        }
        return i;
    }

    public static void addNode(AddNode addNode) {
        System.out.println(Application.class.getCanonicalName() + " adding node " + addNode.getIp() + ":" + addNode.getPort());
        Set<Integer> ports = clusterNodes.get(addNode.getIp());
        if (ports != null) {
            ports.add(addNode.getPort());
        } else {
            ports = new HashSet<>();
            ports.add(addNode.getPort());
            clusterNodes.put(addNode.getIp(), ports);
        }
    }

    public static void main(String[] args) throws IOException {
        Application application = new Application();
        new JCommander(application, args);
        HttpServer server = null;
        try {
            server = startServer();
            Node.getInstance();     //Just to create an instance and initialise
            //new HeartBeatJob().sendHeartBeat();
            System.out.println(String.format("Jersey app started with WADL available at "
                    + "%sapplication.wadl\nHit enter to stop it...", uri));
            System.in.read();
            deleteNode();
        } catch (IOException io) {
            //TODO log
        } finally {
            if (server != null) {
                server.shutdownNow();
            }
            deleteNode();
        }
    }
}
