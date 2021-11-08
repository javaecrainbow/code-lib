package com.salk.lib.netty.rpc;

import java.lang.reflect.Proxy;

import com.salk.lib.netty.rpc.config.ServiceConsumerConfig;

import com.salk.lib.netty.rpc.protocol.ProtocolData;
import com.salk.lib.netty.rpc.protocol.Response;
import io.netty.channel.Channel;

/**
 * Created by 18073747 on 2018/7/17.
 */
public class ServiceProxyManager {
    //TODO 代理层可以做缓存
    public static <T> T doProxy(ServiceConsumerConfig config) {
        ServiceProxy proxy = new ServiceProxy();
        ServiceProxyContext proxyContext = new ServiceProxyContext(proxy, config);
        proxy.setServiceProxyContext(proxyContext);
        return (T) Proxy.newProxyInstance(ServiceProxyManager.class.getClassLoader(),
                new Class<?>[] { config.getServiceInterface() }, proxy);

    }
    /**
     * 接收原始响应数据，反序列化后投递
     * 到相应 {@link ServiceProxy}
     *
     * @param protocolData
     *            响应原始数据
     * @param channel
     *            来自链路
     */
    public static void receiveResponseRawData(ProtocolData protocolData, Channel channel) {
            handleResponseRawData(protocolData, channel);
    }

    static void handleResponseRawData(ProtocolData protocolData, Channel channel) {
        Response response = protocolData.getSerializeType().getSerialization()
                .deserialize(protocolData.getBodyData(), Response.class);
        response.setBodyData(protocolData.getBodyData());
        response.setProtocolVersion(protocolData.getProtocolVersion());
        response.setSerializeType(protocolData.getSerializeType());
        ServiceProxy serviceProxy = channel.attr(ChannelAttrs.attr_serviceProxy).get();
        serviceProxy.receiveResponse(response,channel);
    }
}
