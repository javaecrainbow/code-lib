package com.salk.lib.netty.rpc.server;

import com.salk.lib.netty.rpc.protocol.Response;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 数据包格式
 * Created by 18073747 on 2018/7/25.
 */
public class ResponseEncoder extends MessageToByteEncoder<Response> {
    public static final String RPC = "3";
    public static final String PING = "1";

    @Override
    protected void encode(ChannelHandlerContext ctx, Response response, ByteBuf out) throws Exception {
        if (isPing(response)) {
            out.writeByte(Byte.MAX_VALUE);
        } else {
            byte[] bodyData = response.getBodyData();
            int bodyDataLength = bodyData.length;
            byte[] headData = new byte[] { response.getProtocolVersion().value(), response.getSerializeType().value(), (byte) (bodyDataLength >> 24),
                    (byte) (bodyDataLength >>> 16), (byte) (bodyDataLength >>> 8), (byte) bodyDataLength };
            out.writeBytes(headData);
            out.writeBytes(bodyData);
        }
    }

    /**
     * 是否为PING响应
     */
    public static boolean isPing(Response response) {
        if (response == null)
            return false;
        return PING.equals(response.getType());
    }
}
