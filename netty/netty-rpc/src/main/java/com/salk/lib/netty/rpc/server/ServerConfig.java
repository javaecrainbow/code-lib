package com.salk.lib.netty.rpc.server;

/**
 * Created by 18073747 on 2018/7/25.
 */
public class ServerConfig {
    private int listenPort;
    private long serveTimeOut = 60000;
    public ServerConfig(int listenPort) {
        this.listenPort = listenPort;
    }

    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    public long getServeTimeOut() {
        return serveTimeOut;
    }

    public void setServeTimeOut(long serveTimeOut) {
        this.serveTimeOut = serveTimeOut;
    }
}
