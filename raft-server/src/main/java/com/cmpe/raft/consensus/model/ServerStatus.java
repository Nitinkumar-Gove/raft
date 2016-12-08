package com.cmpe.raft.consensus.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Sushant on 07-12-2016.
 */
@XmlRootElement
public class ServerStatus {
    private String host;
    private Integer port;
    private String status;

    public ServerStatus() {
    }

    public ServerStatus(String host, Integer port, String status) {
        this.host = host;
        this.port = port;
        this.status = status;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
