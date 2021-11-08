package com.salk.lib.netty.rpc.server;

import java.util.List;

import com.salk.lib.netty.rpc.Request;
import com.salk.lib.netty.rpc.protocol.ProtoStuffSerializattion;
import com.salk.lib.netty.rpc.protocol.ProtocolData;
import com.salk.lib.netty.rpc.protocol.ProtocolVersion;
import com.salk.lib.netty.rpc.protocol.SerializeType;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 请求包解码器 Created by 18073747 on 2018/7/25.
 */
public class RequestDecoder extends ByteToMessageDecoder {

    /**
     * 数据包基本长度
     */

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf protocolDataBuf, List<Object> out) throws Exception {
        ProtocolVersion v2 = ProtocolVersion.V2;
        ProtocolData protocolData = null;
        protocolData = extractV2RequestProtocolData2(protocolDataBuf);
        if (protocolData != null) {
            Channel channel = ctx.channel();
            System.out.println("recive request:" + protocolData.getBodyData() + "(size:"
                + protocolData.getBodyData().length + ") from " + channel);
            ServiceInvokeManager.reciveRequestRawData(protocolData, channel);
        }

    }

    /**
     * 获取V2的请求字节数组
     *
     * @param protocolDataBuf
     * @see ProtocolVersion#V2
     * @return {@link Request}，数据不完整返回null
     */
    ProtocolData extractV2RequestProtocolData(ByteBuf protocolDataBuf) {
        int headDataLength = 6;
        int protocolDataLength = protocolDataBuf.readableBytes();
        // incomplete head
        if (protocolDataLength < headDataLength) {
            return null;
        }

        // incomplete body
        // if (protocolDataLength < bodyDataLength + headDataLength) {
        // return null;
        // }
        ProtocolData data = new ProtocolData();
        data.setProtocolVersion(ProtocolVersion.V2);

        // move point to body
        // protocolDataBuf.readByte();// 1byte
        // 根据数据协议获取序列化类型
        // data.setSerializeType(SerializeType.valueOf(protocolDataBuf.readByte()));// 1byte
        // protocolDataBuf.readInt();// 4byte

        byte[] rawBodyData = new byte[176];
        protocolDataBuf.readBytes(rawBodyData);
        data.setBodyData(rawBodyData);
        Request deserialize = new ProtoStuffSerializattion().deserialize(rawBodyData, Request.class);
        return data;
    }

    ProtocolData extractV2RequestProtocolData2(ByteBuf protocolDataBuf) {
        int headDataLength = 6;
        int protocolDataLength = protocolDataBuf.readableBytes();
        // incomplete head
        if (protocolDataLength < headDataLength)
            return null;

        int bodyDataLength = protocolDataBuf.getInt(protocolDataBuf.readerIndex() + 2);
        // incomplete body
        if (protocolDataLength < bodyDataLength + headDataLength)
            return null;

        ProtocolData data = new ProtocolData();
        data.setProtocolVersion(ProtocolVersion.V2);

        // move point to body
        protocolDataBuf.readByte();// 1byte
        data.setSerializeType(SerializeType.valueOf(protocolDataBuf.readByte()));// 1byte
        protocolDataBuf.readInt();// 4byte

        byte[] rawBodyData = new byte[bodyDataLength];
        protocolDataBuf.readBytes(rawBodyData);

        data.setBodyData(rawBodyData);
        return data;
    }
}