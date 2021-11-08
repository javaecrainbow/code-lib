package com.salk.lib.netty.rpc.server;


import com.salk.lib.netty.rpc.config.ServiceProviderConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by 18073747 on 2018/7/17.
 */
public class Server {

    private static ServerBootstrap nettyBootstrap;

    private ServerConfig serverConfig;

    private ServiceProviderConfig serviceProviderConfig;

    public Server(ServerConfig serverConfig, ServiceProviderConfig serviceProviderConfig) {
        this.serverConfig = serverConfig;
        this.serviceProviderConfig = serviceProviderConfig;
    }

    public void start() {
        int listenPort = serverConfig.getListenPort();
        doBootStrap(listenPort);
        //初始化参数
        ServiceInvokeManager.serviceProviderConfigMap.put(serviceProviderConfig.getServiceInterfaceName(),
                serviceProviderConfig);
        System.out.println("server started==========");
    }

    private void doBootStrap(int bindPort) {
        nettyBootstrap = new ServerBootstrap();
        EventLoopGroup boosEventLoopGroup = new NioEventLoopGroup(1);
        EventLoopGroup workEventLoopGroup = new NioEventLoopGroup();
        nettyBootstrap.group(boosEventLoopGroup, workEventLoopGroup).channel(NioServerSocketChannel.class);
        nettyBootstrap.childHandler(new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline channelPipeline = ch.pipeline();
                channelPipeline.addLast("responseEncoder", new ResponseEncoder());
                channelPipeline.addLast("requestDecoder", new RequestDecoder());
            }
        });
        // parent channelOption
        nettyBootstrap.option(ChannelOption.SO_BACKLOG, 200);
        nettyBootstrap.option(ChannelOption.SO_REUSEADDR, true);
        // child channelOption
        nettyBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        nettyBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        nettyBootstrap.childOption(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT);
        ChannelFuture cf = nettyBootstrap.bind(bindPort);
        try {
            cf.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void doShutdown() {
        nettyBootstrap.childGroup().shutdownGracefully();
        nettyBootstrap.group().shutdownGracefully();
        nettyBootstrap = null;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public ServiceProviderConfig getServiceProviderConfig() {
        return serviceProviderConfig;
    }

    public void setServiceProviderConfig(ServiceProviderConfig serviceProviderConfig) {
        this.serviceProviderConfig = serviceProviderConfig;
    }
}
