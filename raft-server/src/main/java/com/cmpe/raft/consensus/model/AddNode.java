package com.cmpe.raft.consensus.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Sushant on 30-11-2016.
 */
@XmlRootElement
public class AddNode {
    private String ip;
    private Integer port;

    public AddNode() {
    }

    public AddNode(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "AddNode{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
