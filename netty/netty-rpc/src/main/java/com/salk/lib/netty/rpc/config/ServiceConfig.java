package com.salk.lib.netty.rpc.config;

/**
 * Created by 18073747 on 2018/7/17.
 */
public class ServiceConfig {
    protected transient Class<?> serviceInterface;
    protected String serviceInterfaceName;
    public String getServiceInterfaceName() {
        if (serviceInterfaceName == null && serviceInterface != null) {
            serviceInterfaceName = serviceInterface.getCanonicalName();
        }
        return serviceInterfaceName;
    }
    public Class<?> getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
        this.serviceInterfaceName = serviceInterface.getCanonicalName();
    }
}
