package com.salk.lib.netty.rpc.config;

/**
 * Created by 18073747 on 2018/7/17.
 */
public class ServiceProviderConfig extends ServiceConfig {
    private transient Object service;

    public ServiceProviderConfig(){

    }
    public ServiceProviderConfig(Class<?> serviceInterface, Object service) {
        setServiceInterface(serviceInterface);
        setService(service);
    }

    public Object getService() {
        return service;
    }

    public void setService(Object service) {
        this.service = service;
    }
    public void copyServiceConfigFrom(ServiceConfig sourceServiceConfig) {
        setServiceInterface(sourceServiceConfig.getServiceInterface());
    }
}
