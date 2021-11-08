package com.salk.lib.netty.rpc.server;

import com.salk.lib.netty.rpc.config.ServiceProviderConfig;
import com.salk.lib.netty.rpc.demo.TestImpl;
import com.salk.lib.netty.rpc.demo.TestIntf;

/**
 * Created by 18073747 on 2018/7/25.
 */
public class ServerTest {
    public static void main(String[] args) {
        ServerConfig serverConfig=new ServerConfig(2288);
        ServiceProviderConfig serviceProviderConfig=new ServiceProviderConfig(TestIntf.class,new TestImpl());
        Server server=new Server(serverConfig,serviceProviderConfig);
        server.start();
    }
}
