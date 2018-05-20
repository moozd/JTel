package com.jtel.common.db;

/**
 * This file is part of jTelClient
 * IntelliJ idea.
 * Date     : 7/23/2016
 * Package : com.jtel.common.db
 *
 * @author <a href="mailto:mohammad.mdz72@gmail.com">Mohammad Mohammad Zade</a>
 */

public class ConfDc {
    int dcId;
    String ip;
    int port;

    public ConfDc(int dcId, String ip, int port) {
        this.dcId = dcId;
        this.ip = ip;
        this.port = port;
    }


    public int getDcId() {
        return dcId;
    }

    public void setDcId(int dcId) {
        this.dcId = dcId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
