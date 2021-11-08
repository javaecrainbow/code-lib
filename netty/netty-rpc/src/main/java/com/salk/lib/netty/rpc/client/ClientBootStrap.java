package com.salk.lib.netty.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by 18073747 on 2018/7/17.
 */
public class ClientBootStrap {
    private static Bootstrap bootstrap = new Bootstrap();
    private static final ClientBootStrap singleton = new ClientBootStrap();

    public static ClientBootStrap clientBootStrap() {
        return singleton;
    }

    private ClientBootStrap(){
        doBootStrap();
    }


    public  void doBootStrap() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        bootstrap.group(workerGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline channelPipeline = ch.pipeline();
                channelPipeline.addLast("requestEncoder", new RequestEncoder());
                channelPipeline.addLast("responseDecoder", new ResponseDecoder());
            }
        });
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        // pooled buffer
        bootstrap.option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT);
    }
    public ChannelFuture connect(String inetHost, int inetPort) {
        return bootstrap.connect(inetHost, inetPort);
    }
}
