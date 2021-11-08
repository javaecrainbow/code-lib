package com.salk.lib.netty.rpc;

import com.salk.lib.netty.rpc.config.ServiceProviderConfig;

/**
 * Created by 18073747 on 2018/7/17.
 */
public class ServiceProvider {
    private String ip;
    private int port;
    private ServiceProviderConfig serviceProviderConfig;

    public ServiceProvider(String ip, int port, ServiceProviderConfig serviceProviderConfig) {
        super();
        this.ip = ip;
        this.port = port;
        this.serviceProviderConfig = serviceProviderConfig;
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

    public ServiceProviderConfig getServiceProviderConfig() {
        return serviceProviderConfig;
    }

    public void setServiceProviderConfig(ServiceProviderConfig serviceProviderConfig) {
        this.serviceProviderConfig = serviceProviderConfig;
    }
}
