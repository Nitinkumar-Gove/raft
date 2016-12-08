package com.cmpe.raft.consensus.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Sushant on 06-12-2016.
 */
@XmlRootElement
public class ClientRequest {
    private String action;
    private Person person;

    public ClientRequest() {
    }

    public ClientRequest(String type, Person person) {
        this.action = type;
        this.person = person;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
