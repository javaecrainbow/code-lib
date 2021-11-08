package com.salk.lib.netty.rpc.protocol;

/**
 * Created by 18073747 on 2018/7/25.
 */
public interface ISerialization {
    /**
     * protobuf序列化版本2
     */
    ISerialization PROTOBUF = new ProtoStuffSerializattion();

    byte[] serialize(ProtocolData protocolData);

    /**
     * 协议数据载体序列化
     *
     * @param bytes
     * @param typeClass
     * @return 反序列化后的对象
     */
    <T extends ProtocolData> T deserialize(byte[] bytes, Class<T> typeClass);
}
