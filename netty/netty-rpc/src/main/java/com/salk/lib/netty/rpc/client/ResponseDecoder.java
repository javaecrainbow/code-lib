package com.salk.lib.netty.rpc.client;

import java.util.List;

import com.salk.lib.netty.rpc.ServiceProxyManager;
import com.salk.lib.netty.rpc.protocol.ProtocolData;
import com.salk.lib.netty.rpc.protocol.ProtocolVersion;
import com.salk.lib.netty.rpc.protocol.SerializeType;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 响应包解码器
 * 
 * <pre>
 * Created by 18073747 on 2018/7/25.
 */
public class ResponseDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf protocolDataBuf, List<Object> responses) throws Exception {
        ProtocolData rawResponse = null;
        ProtocolVersion protocolVersion = ProtocolVersion.V2;
        int headDataLength = 6;
        int protocolDataLength = protocolDataBuf.readableBytes();
        // incomplete head
        if (protocolDataLength < headDataLength)
            return;

        int bodyDataLength = protocolDataBuf.getInt(protocolDataBuf.readerIndex() + 2);
        // incomplete body
        if (protocolDataLength < bodyDataLength + headDataLength)
            return;

        // move point to body
        protocolDataBuf.readByte();// 1byte
        SerializeType serializeType = SerializeType.valueOf(protocolDataBuf.readByte());// 1byte
        protocolDataBuf.readInt();// 4byte

        byte[] bodyData = new byte[bodyDataLength];
        protocolDataBuf.readBytes(bodyData);

        rawResponse = new ProtocolData();
        rawResponse.setProtocolVersion(protocolVersion);
        rawResponse.setSerializeType(serializeType);
        rawResponse.setBodyData(bodyData);
        if (rawResponse != null) {
            ServiceProxyManager.receiveResponseRawData(rawResponse, ctx.channel());
        }
    }

}
