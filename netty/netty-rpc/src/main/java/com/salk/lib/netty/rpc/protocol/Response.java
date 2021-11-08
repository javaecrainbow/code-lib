package com.salk.lib.netty.rpc.protocol;

/**
 * 响应对象，响应协议数据的载体
 * Created by 18073747 on 2018/7/25.
 */
public class Response extends ProtocolData {
    public static final int MAX_LENGTH = 1024;
    private long requestId;
    private Object rpcResult;
    private Throwable rpcException;
    private String type;

    /**
     * for JsonSerialization
     */
    public Response() {
    }

    Response(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public Object getRpcResult() {
        return rpcResult;
    }

    public void setRpcResult(Object rpcResult) {
        this.rpcResult = rpcResult;
    }


    public Throwable getRpcException() {
        return rpcException;
    }

    public void setRpcException(Throwable rpcException) {
        this.rpcException = rpcException;
    }

    Object getLiteRpcResult() {
        if (rpcResult == null) {
            return null;
        }
        if (bodyData.length > MAX_LENGTH) {
            return rpcResult.getClass().getName() + "@" + rpcResult.hashCode();
        }
        return rpcResult;
    }

}
