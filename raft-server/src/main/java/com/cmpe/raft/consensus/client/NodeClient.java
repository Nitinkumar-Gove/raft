package com.cmpe.raft.consensus.client;

import com.cmpe.raft.consensus.app.Application;
import com.cmpe.raft.consensus.model.AddNode;
import com.cmpe.raft.consensus.model.HeartBeat;
import com.cmpe.raft.consensus.model.Person;
import com.cmpe.raft.consensus.model.Vote;
import com.cmpe.raft.consensus.node.Node;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sushant on 27-11-2016.
 */
public class NodeClient {

    private String host;
    private Integer port;
    private CloseableHttpClient client = null;
    private String apiURL = null;
    private String base_uri= null;

    public NodeClient(String host, Integer port) {
        this.host = host;
        this.port = port;
        initialize();
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public void updateHost(String host, Integer port) {
        this.host = host;
        this.port = port;
        initialize();
    }

    private void initialize() {
        client = HttpClients.createDefault();
        apiURL = "http://" + host + ":" + port + "/raft/node/%s?ip="+ Application.getIp() +"&port="+Application.getPort()+"&term=%d"; // Using sample instead of g
        base_uri= "http://" + host + ":" + port + "/raft/node";
    }

    public HeartBeat sendHeartBeat() {
        String getHttpUri = String.format(apiURL, "heartbeat", Node.getInstance().getTerm());
        System.out.println(NodeClient.class.getCanonicalName()+" Get URI "+ getHttpUri);
        HttpGet httpGet = new HttpGet(getHttpUri);
        httpGet.setHeader("Accept", "application/json");
        HeartBeat heartBeat = null;
        try {
            HttpResponse httpResponse = client.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String content = EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
            JSONObject jsonContent = new JSONObject(content);
            ObjectMapper mapper = new ObjectMapper();
            heartBeat = mapper.readValue(jsonContent.toString(), HeartBeat.class);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                client.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return heartBeat;
    }

    public Vote sendCandidacyRequest() {
        String getHttpUri = String.format(apiURL, "leader", Node.getInstance().getTerm());
        System.out.println(NodeClient.class.getCanonicalName()+" Get URI "+ getHttpUri);
        HttpGet httpGet = new HttpGet(getHttpUri);
        httpGet.setHeader("Accept", "application/json");
        Vote vote = null;
        try {
            HttpResponse httpResponse = client.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String content = EntityUtils.toString(httpEntity);
            System.out.println(NodeClient.class.getCanonicalName() + " Candidacy response "+ content);
            EntityUtils.consume(httpEntity);
            JSONObject jsonContent = new JSONObject(content);
            ObjectMapper mapper = new ObjectMapper();
            vote = mapper.readValue(jsonContent.toString(), Vote.class);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                client.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return vote;
    }

    public AddNode sendAddNodeRequest() {
        AddNode addNode= new AddNode(Application.getIp(), Application.getPort());
        Entity<AddNode> addNodeEntity = Entity.json(addNode);
        Client client = ClientBuilder.newClient();
        WebTarget baseTarget = client.target(base_uri);
        Response postResponse = baseTarget
                .request(MediaType.APPLICATION_JSON)
                .post(addNodeEntity);
        AddNode newAddNode = postResponse.readEntity(AddNode.class);
        return newAddNode;
    }

    public Person sendDoPostRequest(Person person) {
        Entity<Person> entityPerson = Entity.json(person);
        Client client = ClientBuilder.newClient();
        WebTarget baseTarget = client.target(base_uri);
        Response postResponse = baseTarget
                .path("person")
                .request(MediaType.APPLICATION_JSON)
                .post(entityPerson);
        Person newPerson = postResponse.readEntity(Person.class);
        return newPerson;
    }

    public Person sendDoPutRequest(Person person) {
        Entity<Person> entityPerson = Entity.json(person);
        Client client = ClientBuilder.newClient();
        WebTarget baseTarget = client.target(base_uri);
        Response postResponse = baseTarget
                .path("person")
                .path(person.getId().toString())
                .request(MediaType.APPLICATION_JSON)
                .put(entityPerson);
        Person newPerson = postResponse.readEntity(Person.class);
        return newPerson;
    }

    public List<Person> sendDoGetAllRequest() {
        Client client = ClientBuilder.newClient();
        WebTarget baseTarget = client.target(base_uri);
        Response postResponse = baseTarget
                .path("person")
                .request(MediaType.APPLICATION_JSON)
                .get();
        System.out.println(NodeClient.class.getCanonicalName() + " response from get all "+ postResponse.getStatus());
        String persons = postResponse.readEntity(String.class);
        List<Person> personList = getPersonList(persons);
        return personList;
    }

    private List<Person> getPersonList(String persons) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Person> personList = mapper.readValue(persons,
                    TypeFactory.defaultInstance().constructCollectionType(List.class,
                            Person.class));
            return personList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Person sendDoGetRequest(Integer id) {
        Client client = ClientBuilder.newClient();
        WebTarget baseTarget = client.target(base_uri);
        Response postResponse = baseTarget
                .path("person")
                .path(id.toString())
                .request(MediaType.APPLICATION_JSON)
                .get();
        Person person = postResponse.readEntity(Person.class);
        return person;
    }

    public Person sendDoDeleteRequest(Integer id) {
        Client client = ClientBuilder.newClient();
        WebTarget baseTarget = client.target(base_uri);
        Response postResponse = baseTarget
                .path("person")
                .path(id.toString())
                .request(MediaType.APPLICATION_JSON)
                .delete();
        Person person = postResponse.readEntity(Person.class);
        return person;
    }

    public static void main(String[] args) {
        NodeClient nodeClient = new NodeClient("localhost", 8080);
        HeartBeat heartBeat = nodeClient.sendHeartBeat();
        System.out.println(NodeClient.class.getCanonicalName()+" Heart beat response: "+ heartBeat);
        nodeClient.sendCandidacyRequest();
    }
}