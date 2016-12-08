package com.cmpe.raft.consensus.resource;

import com.cmpe.raft.consensus.dao.Dao;
import com.cmpe.raft.consensus.error.Error;
import com.cmpe.raft.consensus.model.*;
import com.cmpe.raft.consensus.node.Node;
import com.cmpe.raft.consensus.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import org.codehaus.jettison.json.JSONArray;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sushant on 26-11-2016.
 */
@Path("/node")
@Singleton                  // It is very important that this resource is kept Singleton
public class NodeResource {
    private Node node = Node.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/heartbeat")
    public Response heartBeat(@QueryParam("ip") String ip, @QueryParam(value = "port") Integer port, @QueryParam(value = "term") Long term) {
        System.out.println(NodeResource.class.getCanonicalName()+ " received heart beat from "+ ip+":"+port);
        if (StringUtil.isEmpty(ip) || port == null || term == null)
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Error.INVALID_REQUEST_MISSING_QUERY_PARAM)
                    .build();
        HeartBeat heartBeat = node.reactToHeartBeat(term, ip, port);
        return Response.ok()
                .entity(heartBeat)
                .build();
    }


    @GET
    @Path("/leader")
    @Produces(MediaType.APPLICATION_JSON)
    public Response leaderRequest(@QueryParam("ip") String ip, @QueryParam(value = "port") Integer port, @QueryParam(value = "term") Long term) {
        if (StringUtil.isEmpty(ip) || port == null || term == null)
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Error.INVALID_REQUEST_MISSING_QUERY_PARAM)
                    .build();
        Vote vote = node.reactToLeaderRequest(term);
        return Response.ok()
                .entity(vote)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNode(AddNode addNode) {
        System.out.println(NodeResource.class.getCanonicalName()
                + " received reactToNode " + addNode.getIp() + ":" + addNode.getPort());
        AddNode addNodeResponse = node.reactToAddNode(addNode);
        return Response.created(URI.create(""))
                .entity(addNodeResponse)
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response doGetStatus() {
        Status status = new Status(node.getStatus());
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity(status)
                .build();
    }


    @GET
    @Path("/person")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> doGetAll() {
        List<Person> persons = Dao.getAll();
        return persons;
    }

    @GET
    @Path("/person/{person_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response doGetWork(@PathParam("person_id") Integer id) {
        Person person = Dao.getPerson(id);
        return Response.ok()
                .entity(person)
                .build();
    }

    @PUT
    @Path("/person/{person_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response doPut(@PathParam("person_id") Integer id, Person person) {
        person.setId(id);
        Person newPerson = Dao.updatePerson(person);
        return Response.accepted()
                .entity(newPerson)
                .build();
    }

    @POST
    @Path("/person")
    @Produces(MediaType.APPLICATION_JSON)
    public Response doPost(Person person) {
        System.out.println(NodeResource.class.getCanonicalName()+ " adding new Person");
        Person newPerson = Dao.addPerson(person);
        return Response.status(Response.Status.CREATED)
                .entity(newPerson)
                .build();
    }

    @DELETE
    @Path("/person/{person_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response doPut(@PathParam("person_id") Integer id) {
        Person person = Dao.deletePerson(id);
        return Response.accepted()
                .entity(person)
                .build();
    }
}
