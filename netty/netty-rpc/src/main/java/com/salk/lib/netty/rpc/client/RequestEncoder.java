package com.salk.lib.netty.rpc.client;


import com.salk.lib.netty.rpc.Request;
import com.salk.lib.netty.rpc.protocol.ProtocolVersion;
import com.salk.lib.netty.rpc.protocol.SerializeType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 请求包编码器
 * Created by 18073747 on 2018/7/25.
 * <pre>
 */
public class RequestEncoder extends MessageToByteEncoder<Request> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Request request, ByteBuf out) throws Exception {
        byte[] bodyData = request.getBodyData();
        byte[] headData = encodeHeadData(request.getProtocolVersion(), request.getSerializeType(), bodyData.length);
        out.writeBytes(headData);
        out.writeBytes(bodyData);
    }
    static byte[] encodeHeadData(ProtocolVersion protocolVersion, SerializeType serializeType, int bodyDataLength) {
        return new byte[] { protocolVersion.value(), serializeType.value(), (byte) (bodyDataLength >>> 24),
                (byte) (bodyDataLength >>> 16), (byte) (bodyDataLength >>> 8), (byte) bodyDataLength };
    }
}
