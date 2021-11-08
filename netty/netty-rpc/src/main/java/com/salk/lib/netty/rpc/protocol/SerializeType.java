package com.salk.lib.netty.rpc.protocol;

/**
 * Created by 18073747 on 2018/7/25.
 */
public enum SerializeType {
    PROTOBUF(ProtocolData.PROTOBUF_CODE, ISerialization.PROTOBUF);

    byte typeValue;

    ISerialization serializer;

    static final SerializeType[] allType = SerializeType.values();

    private SerializeType(int typeValue, ISerialization serializer) {
        this.typeValue = (byte) typeValue;
        this.serializer = serializer;
    }

    public byte value() {
        return typeValue;
    }

    public ISerialization getSerialization() {
        return serializer;
    }

    public static SerializeType valueOf(byte typeValue) {
        for (SerializeType type : allType) {
            if (typeValue == type.value()) {
                return type;
            }
        }
        return null;
    }
}
