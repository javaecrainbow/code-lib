package com.salk.lib.netty.rpc.client;


import com.salk.lib.netty.rpc.ServiceProxyManager;
import com.salk.lib.netty.rpc.config.ServiceConsumerConfig;
import com.salk.lib.netty.rpc.demo.Result;
import com.salk.lib.netty.rpc.demo.TestIntf;

/**
 * Created by 18073747 on 2018/7/25.
 */
public class ClientTest {
    public static void main(String[] args) {
            ServiceConsumerConfig serviceConsumerConfig=new ServiceConsumerConfig();
            serviceConsumerConfig.setServiceProviderURL("127.0.0.1:2288");
            serviceConsumerConfig.setMaxConnectionNumPerProvider(100);
            serviceConsumerConfig.setServiceInterface(TestIntf.class);
            TestIntf service= ServiceProxyManager.doProxy(serviceConsumerConfig);
            for(int i=0;i<10000000;i++) {
                Result hello = service.hello("test 124");
                System.out.println("execute times========" + i);
                System.out.println("result========" + hello.getValue());
            }
    }
}
