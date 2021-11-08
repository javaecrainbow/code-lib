package com.salk.lib.netty.rpc.server;

import com.salk.lib.netty.rpc.config.ServiceProviderConfig;
import com.salk.lib.netty.rpc.util.ClassUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by 18073747 on 2018/7/17.
 */
public class ServiceInvokeContext {

    private ServiceProviderConfig serviceProviderConfig;

    private final Map<Method, String> cachedMangleMethodNameMap = new HashMap<Method, String>();

    private final Map<String, Method> methodMap = new HashMap<String, Method>();

    ServiceInvokeContext(ServiceProviderConfig serviceProviderConfig ) {
        this.serviceProviderConfig = serviceProviderConfig;
        initMethodMap();
    }

    public ServiceProviderConfig getServiceProviderConfig() {
        return serviceProviderConfig;
    }


    void initMethodMap() {
        Class<?> interfaceClazz = serviceProviderConfig.getServiceInterface();
        Method[] allMethods = interfaceClazz.getMethods();
        for (Method method : allMethods) {
            method.setAccessible(true);
            methodMap.put(ClassUtil.mangleMethodName(method), method);
        }
    }

    public Method findMethod(String mangledMethodName) {
        return methodMap.get(mangledMethodName);
    }

}
