package com.salk.lib.netty.rpc.protocol;

import io.netty.buffer.ByteBuf;

/**
 * Created by 18073747 on 2018/7/25.
 */
public enum ProtocolVersion {
    /**
     * 包含请求和响应协议，协议数据都是由head和body2部分组成
     *
     * <pre>
     * 1.head：6个字节{协议版本(1字节)，序列化方式(1字节)，body长度(4字节)}
     * 2.body:{@link Request}或者
     * {@link Response}序列化后的字节数组
     */
    V2(2);
    byte versionValue;
    private ProtocolVersion(int versionValue) {
        this.versionValue = (byte) versionValue;
    }

    public byte value() {
        return versionValue;
    }
    public static String getProtocolHeadString(ByteBuf buf) {
        if (buf.readableBytes() == 1) {
            return "" + buf.getByte(0);
        } else if (buf.readableBytes() > 1) {
            return buf.getByte(0) + "," + buf.getByte(1);
        }
        return "";
    }
}
