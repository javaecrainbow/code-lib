package com.salk.lib.netty.rpc.protocol;

import java.io.Serializable;

/**
 * Created by 18073747 on 2018/7/25.
 */
public class ProtocolData implements Serializable {
    public static  final transient String PROTOBUF_NAME = "protobuf";
    public static  final transient byte PROTOBUF_CODE = 0;
    private static final transient SerializeType configedSerializeType;
    static {
        byte serializationCode = 0;
        configedSerializeType = SerializeType.valueOf(serializationCode);
    }

    protected transient ProtocolVersion protocolVersion = ProtocolVersion.V2;
    protected transient SerializeType serializeType = configedSerializeType;
    protected transient byte[] bodyData;

    public final byte[] getBodyData() {
        return bodyData;
    }

    public final void setBodyData(byte[] bodyData) {
        this.bodyData = bodyData;
    }

    public final ProtocolVersion getProtocolVersion() {
        return protocolVersion;
    }

    public final void setProtocolVersion(ProtocolVersion protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public final SerializeType getSerializeType() {
        return serializeType;
    }

    public final void setSerializeType(SerializeType serializeType) {
        this.serializeType = serializeType;
    }

}
