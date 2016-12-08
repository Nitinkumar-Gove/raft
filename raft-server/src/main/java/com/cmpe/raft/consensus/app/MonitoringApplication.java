package com.cmpe.raft.consensus.app;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.cmpe.raft.consensus.node.Node;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Created by Sushant on 07-12-2016.
 */
public class MonitoringApplication {

    @Parameter(names = {"--ipaddress", "-ip"}, description = "IP address of the node")
    private static String ip = "localhost";
    @Parameter(names = {"--port", "-p"}, description = "Port of the node")
    private static Integer port = 9000;
    private static String uri = "http://%s:%d/raft/";


    public static void main(String[] args) throws IOException {
        Application application = new Application();
        new JCommander(application, args);
        HttpServer server = null;
        uri = String.format(uri, ip, port);
        try {
            final ResourceConfig rc = new ResourceConfig().packages("com.cmpe.raft.consensus.resource.monitor");
            server = GrizzlyHttpServerFactory.createHttpServer(URI.create(uri), rc);
            //new HeartBeatJob().sendHeartBeat();
            System.out.println(String.format("Jersey app started with WADL available at "
                    + "%sapplication.wadl\nHit enter to stop it...", uri));
            System.in.read();
        } catch (IOException io) {
            //TODO log
        } finally {
        }
    }

}
