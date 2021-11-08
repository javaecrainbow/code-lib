package com.salk.lib.netty.rpc;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.salk.lib.netty.rpc.client.ClientBootStrap;
import com.salk.lib.netty.rpc.config.ServiceConsumerConfig;
import com.salk.lib.netty.rpc.config.ServiceProviderConfig;
import com.salk.lib.netty.rpc.util.ClassUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.internal.ConcurrentSet;

/**
 * 服务代理上下文
 */
public final class ServiceProxyContext {
    private final ServiceProvider directedServiceProvider;
    private final ServiceConsumerConfig serviceConsumerConfig;
    private final Random cachedChannelRandom = new Random();
    private final ServiceProxy serviceProxy;
    private final Map<ServiceProvider, Set<Channel>> providerChannelsMap =
        new ConcurrentHashMap<ServiceProvider, Set<Channel>>();
    private final Map<Method, String> cachedMangleMethodNameMap = new HashMap<Method, String>();
    private final Map<String, String> mangleNameToMethodNameMap = new HashMap<String, String>();

    /**
     * 构造方法
     *
     * @param serviceProxy
     * @param consumerConfig
     */
    ServiceProxyContext(ServiceProxy serviceProxy, ServiceConsumerConfig consumerConfig) {
        this.serviceProxy = serviceProxy;
        this.serviceConsumerConfig = consumerConfig;
        this.initMangleMethodNameMapAndMethodMap();
        String directedServiceProviderURL = consumerConfig.getServiceProviderURL();
        if (directedServiceProviderURL != null && directedServiceProviderURL.length() > 0
            && directedServiceProviderURL.contains(":")) {
            String[] ipAndPort = directedServiceProviderURL.trim().split(":");
            if (ipAndPort.length == 2) {
                ServiceProviderConfig serviceProviderConfig = new ServiceProviderConfig();
                serviceProviderConfig.copyServiceConfigFrom(consumerConfig);
                directedServiceProvider =
                    new ServiceProvider(ipAndPort[0], Integer.valueOf(ipAndPort[1]), serviceProviderConfig);
                return;
            }
        }
        directedServiceProvider = null;
    }

    void initMangleMethodNameMapAndMethodMap() {
        Class<?> interfaceClazz = serviceConsumerConfig.getServiceInterface();
        Method[] allMethods = interfaceClazz.getMethods();
        for (Method method : allMethods) {
            String mangleMethodName = ClassUtil.mangleMethodName(method);
            cachedMangleMethodNameMap.put(method, mangleMethodName);
            mangleNameToMethodNameMap.put(mangleMethodName, method.getName());
        }
    }

    /**
     * 从缓存链路中选择,调用此方法必须确保已经有可用缓存的channel
     *
     * @param serviceProvider
     * @return
     */
    Channel selectCachedChannel(ServiceProvider serviceProvider) {
        List<Channel> copyedChannels = new ArrayList<Channel>(providerChannelsMap.get(serviceProvider));
        if (copyedChannels.size() > 1) {
            return copyedChannels.get(cachedChannelRandom.nextInt(copyedChannels.size()));
        }
        return copyedChannels.get(0);
    }

    public Channel selectChannelIfLessthanMaxCreateOnce(ServiceProvider serviceProvider) {
        if (channelSize(serviceProvider) == 0) {
            synchronized (serviceProvider) {
                if (channelSize(serviceProvider) == 0) {
                    return createChannel(serviceProvider);
                } else {
                    return selectCachedChannel(serviceProvider);
                }
            }
        } else {
            Channel selecedChannel = null;
            if (channelSize(serviceProvider) >= serviceConsumerConfig.getMaxConnectionNumPerProvider()) {
                selecedChannel = selectCachedChannel(serviceProvider);
            } else {
                // double check
                synchronized (serviceProvider) {
                    if (channelSize(serviceProvider) < serviceConsumerConfig.getMaxConnectionNumPerProvider()) {
                        selecedChannel = createChannel(serviceProvider);
                    } else {
                        selecedChannel = selectCachedChannel(serviceProvider);
                    }
                }
            }
            return selecedChannel;
        }
    }

    /**
     * 新建链路
     */
    private Channel createChannel(ServiceProvider serviceProvider) {
        Collection<Channel> channels = providerChannelsMap.get(serviceProvider);
        Channel newChannel = null;
        ChannelFuture cf = null;
        try {
            cf = ClientBootStrap.clientBootStrap().connect(serviceProvider.getIp(), serviceProvider.getPort()).await();
            newChannel = cf.channel();
            if (!newChannel.isActive() && newChannel.remoteAddress() == null) {
                throw new NullPointerException("remoteAddress");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ChannelAttrs.setServiceProviderIfAbsent(newChannel, serviceProvider);
        ChannelAttrs.setSeviceProxyIfAbsent(newChannel, serviceProxy);
        ChannelAttrs.getFutureResponseMap(newChannel);
        channels.add(newChannel);
        return newChannel;
    }

    /**
     * 获取与服务提供者建立的链路个数
     *
     * @param serviceProvider
     * @return
     */
    int channelSize(ServiceProvider serviceProvider) {
        Set<Channel> channels = providerChannelsMap.get(serviceProvider);
        if (channels == null) {
            synchronized (serviceProvider) {
                channels = providerChannelsMap.get(serviceProvider);
                if (channels == null) {
                    channels = new ConcurrentSet<Channel>();
                    providerChannelsMap.put(serviceProvider, channels);
                }
            }
        }
        return channels.size();
    }

    /**
     * 判断是否包含此方法
     *
     * @param method
     * @return
     */
    boolean containsMethod(Method method) {
        return cachedMangleMethodNameMap.containsKey(method);
    }

    public ServiceProvider getDirectedServiceProvider() {
        return directedServiceProvider;
    }

    public ServiceConsumerConfig getServiceConsumerConfig() {
        return serviceConsumerConfig;
    }

    public Map<ServiceProvider, Set<Channel>> getProviderChannelsMap() {
        return providerChannelsMap;
    }

    public String getMangleMethodName(Method method) {
        return cachedMangleMethodNameMap.get(method);
    }

    public String getMethodName(String mangleMethodName) {
        return mangleNameToMethodNameMap.get(mangleMethodName);
    }

    Channel selectChannel(ServiceProvider serviceProvider) throws Exception {
        return selectChannelIfLessthanMaxCreateOnce(serviceProvider);
    }

}
