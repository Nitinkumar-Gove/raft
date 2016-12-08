package com.cmpe.raft.consensus.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Sushant on 06-12-2016.
 */
@XmlRootElement
public class Status {
    String name;

    public Status() {
    }

    public Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
