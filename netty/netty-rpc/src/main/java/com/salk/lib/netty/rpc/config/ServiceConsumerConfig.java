package com.salk.lib.netty.rpc.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 18073747 on 2018/7/17.
 */
public class ServiceConsumerConfig extends ServiceConfig {
    /**
     * 服务提供者URL，用于直连服务,格式ip:port，如：192.168.1.1:12468
     */
    private String serviceProviderURL;

    private int maxConnectionNumPerProvider = 1;

    private long defaultMethodTimeOut = 10000;


    private final Map<String, Long> methodTimeOutMap = new HashMap<String, Long>();


    public ServiceConsumerConfig() {
    }

    public String getServiceProviderURL() {
        return serviceProviderURL;
    }

    public void setServiceProviderURL(String serviceProviderURL) {
        this.serviceProviderURL = serviceProviderURL;
    }

    public int getMaxConnectionNumPerProvider() {
        return maxConnectionNumPerProvider;
    }

    public void setMaxConnectionNumPerProvider(int maxConnectionNumPerProvider) {
        this.maxConnectionNumPerProvider = maxConnectionNumPerProvider;
    }
    public void setMethodTimeOutMap(String methodName, Long timeOut) {
        methodTimeOutMap.put(methodName, timeOut);
    }

    public long getMethodTimeOut(String methodName){
        Long timeOut = methodTimeOutMap.get(methodName);
        return timeOut == null ? defaultMethodTimeOut : timeOut;

    }
    public void setMethodTimeOutMap(Map<String, Long> methodTimeOutMap) {
        if (methodTimeOutMap!=null && methodTimeOutMap.size()>0) {
            this.methodTimeOutMap.putAll(methodTimeOutMap);
        }
    }
}
