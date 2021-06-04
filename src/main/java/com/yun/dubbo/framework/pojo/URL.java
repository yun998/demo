package com.yun.dubbo.framework.pojo;

import java.io.Serializable;

public class URL implements Serializable {

    private String hostName;
    private Integer port;

    public URL(String hostName, Integer port) {
        this.hostName = hostName;
        this.port = port;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "URL{" +
                "hostName='" + hostName + '\'' +
                ", port=" + port +
                '}';
    }
}
