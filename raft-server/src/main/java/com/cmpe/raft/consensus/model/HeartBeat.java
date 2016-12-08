package com.cmpe.raft.consensus.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by Sushant on 26-11-2016.
 */
public class HeartBeat {
    private String ip;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss a z")
    private Date date;
    private Integer port;
    private Long term;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Long getTerm() {
        return term;
    }

    public void setTerm(Long term) {
        this.term = term;
    }

    @Override
    public String toString() {
        return "HeartBeat{" +
                "ip='" + ip + '\'' +
                ", date=" + date +
                ", port=" + port +
                ", term=" + term +
                '}';
    }
}
