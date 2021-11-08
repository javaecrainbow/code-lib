package com.salk.lib.netty.rpc.protocol;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.salk.lib.netty.rpc.Request;

/**
 * Created by 18073747 on 2018/7/17.
 */
public class ProtoStuffSerializattion implements ISerialization {
    static final String CHARSET = "UTF-8";

    @Override
    public byte[] serialize(ProtocolData protocolData) {
        if(protocolData instanceof Request) {
            RuntimeSchema<Request> schema = RuntimeSchema.createFrom(Request.class);
            LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
            byte[] bytes = ProtostuffIOUtil.toByteArray((Request)protocolData, schema, buffer);
            return bytes;
        }else{
            RuntimeSchema<Response> schema = RuntimeSchema.createFrom(Response.class);
            LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
            byte[] bytes = ProtostuffIOUtil.toByteArray((Response)protocolData, schema, buffer);
            return bytes;
        }
    }

    @Override
    public <T extends ProtocolData> T deserialize(byte[] bytes, Class<T> typeClass) {

        RuntimeSchema<T> schema = RuntimeSchema.createFrom(typeClass);
        try {
            T o = (T) typeClass.newInstance();
            ProtostuffIOUtil.mergeFrom(bytes, o, schema);
            return o;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
