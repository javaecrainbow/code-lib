package com.salk.lib.netty.rpc.server;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.salk.lib.netty.rpc.Request;
import com.salk.lib.netty.rpc.config.ServiceProviderConfig;
import com.salk.lib.netty.rpc.protocol.ProtocolData;
import com.salk.lib.netty.rpc.protocol.ProtocolVersion;
import com.salk.lib.netty.rpc.protocol.Response;
import com.salk.lib.netty.rpc.protocol.SerializeType;
import io.netty.channel.Channel;

/**
 * Created by 18073747 on 2018/7/17.
 */
public class ServiceInvokeManager {
    static final Map<String, ServiceInvokeContext> allServiceInvokeContextMap = new HashMap<String, ServiceInvokeContext>();
    public static final Map<String, ServiceProviderConfig> serviceProviderConfigMap = new ConcurrentHashMap<String, ServiceProviderConfig>();

    /**
     * 接受请求原生数据、反序列化请求并 {@link #dispathRequest(Request, Channel)}
     *
     * @param protocolData
     * @param channel
     */
    static void reciveRequestRawData(final ProtocolData protocolData, final Channel channel) {
        Thread deserializeTask=new Thread(new Runnable() {
            public void run() {
                Request request = null;
                ProtocolVersion protocolVersion = protocolData.getProtocolVersion();
                SerializeType serializeType = protocolData.getSerializeType();
                byte[] bodyData = protocolData.getBodyData();

                try {
                    if (ProtocolVersion.V2 == protocolVersion) {
                        request = serializeType.getSerialization().deserialize(bodyData, Request.class);
                        request.setProtocolVersion(ProtocolVersion.V2);
                        request.setSerializeType(serializeType);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (request != null) {
                    try {
                        dispathRequest(request, channel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        deserializeTask.start();
    }


    static void dispathRequest(final Request request, final Channel channel) throws Exception {
        ProtocolVersion protocolVersion = request.getProtocolVersion();
        SerializeType serializeType = request.getSerializeType();
        final long requestID = request.getRequestId();

        String interfaceName = request.getInterfaceName();
        String mangledMethodName = request.getMangledMethodName();
        Object result = null;
        //todo 提取到服务端进行初始化
        ServiceProviderConfig serviceProviderConfig = serviceProviderConfigMap.get(request.getInterfaceName());
        ServiceInvokeContext serviceInvokeContext=new ServiceInvokeContext(serviceProviderConfig);
        Method targetMethod = serviceInvokeContext.findMethod(mangledMethodName);
        if (targetMethod != null) {
            Object serviceObject = serviceInvokeContext.getServiceProviderConfig().getService();
            try {
                result = targetMethod.invoke(serviceObject, request.getArgs());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Response response = new Response();
        response.setProtocolVersion(protocolVersion);
        response.setSerializeType(serializeType);
        response.setRequestId(requestID);
        response.setRpcResult(result);
        response.setRpcException(new UnsupportedOperationException("no such service[" + interfaceName + "]"));
        sendResponse(response, channel);
        return;
    }

    /**
     * 通过 {@link Channel} 推送 {@link Response}
     *
     * @param response
     * @param channel
     */
    static void sendResponse(Response response, Channel channel) {
        try {
            serializeResponseAndSet(response);
        } catch (IOException e) {
            return;
        }
        channel.writeAndFlush(response, channel.voidPromise());
    }

    /**
     * 序列化 {@link Response}，并将字节数组设置到 {@link Response#setBodyData(byte[])}
     *
     * @param response
     * @throws IOException
     * @throws Exception
     */
    static void serializeResponseAndSet(Response response) throws IOException {
        byte[] bodyData = null;
        if (response.getProtocolVersion().equals(ProtocolVersion.V2)) {
            bodyData = serializeV2Response(response);
        }
        response.setBodyData(bodyData);
    }

    /**
     * 序列化V2响应
     *
     * @param response
     * @throws Exception
     */
    static byte[] serializeV2Response(Response response) {
        return response.getSerializeType().getSerialization().serialize(response);
    }
}
