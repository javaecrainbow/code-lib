package com.salk.lib.netty.rpc;

import com.salk.lib.netty.rpc.protocol.ProtocolData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 请求对象，请求协议数据的载体
 */
public final class Request extends ProtocolData {
    private static final long serialVersionUID = -1893441571595202278L;
    private static final transient AtomicLong requestIdGenerator = new AtomicLong(0);
    // means it must greater than 0
    private long requestId = requestIdGenerator.incrementAndGet();
    /**
     * need class.getCanonicalName()
     */
    private String interfaceName;
    private String mangledMethodName;
    private Object[] args;
    private Map<Object, Object> propertyMap;

    public Request() {
    }

    public Request(String interfaceName, String mangledMethodName, Object[] args) {
        this.interfaceName = interfaceName;
        this.mangledMethodName = mangledMethodName;
        this.args = args;
    }

    /**
     * @param key
     * @return
     * @see Map#get(Object)
     */
    public Object getProperty(Object key) {
        if (propertyMap == null) {
            return null;
        }
        return propertyMap.get(key);
    }

    public String getPropertyAsString(Object key) {
        Object v = getProperty(key);
        if (v == null) {
            return null;
        }
        return String.valueOf(v);
    }

    /**
     * @param key
     * @param value
     * @return
     * @see Map#put(Object, Object)
     */
    public Object setProperty(Object key, Object value) {
        if (propertyMap == null) {
            propertyMap = new HashMap<Object, Object>(5);
        }
        return propertyMap.put(key, value);
    }

    /**
     * @return the property
     */
    public Map<Object, Object> getProperyMap() {
        return propertyMap;
    }

    /**
     * @param property the property to set
     */
    public void setProperty(Map<Object, Object> property) {
        this.propertyMap = property;
    }

    public boolean containsProperty(Object key) {
        if (key == null || propertyMap == null) {
            return false;
        }
        return propertyMap.containsKey(key);
    }

    public void setRequestId(long id) {
        this.requestId = id;
    }

    public long getRequestId() {
        return requestId;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getMangledMethodName() {
        return mangledMethodName;
    }

    public void setMangledMethodName(String mangledMethodName) {
        this.mangledMethodName = mangledMethodName;
    }

    /**
     * @param interfaceName
     * @see #interfaceName
     */
    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "Request [requestId=" + requestId + ", interfaceName=" + interfaceName + ", mangledMethodName="
                + mangledMethodName + ", args=" + Arrays.toString(args) + ", property=" + propertyMap + "]";
    }

}
