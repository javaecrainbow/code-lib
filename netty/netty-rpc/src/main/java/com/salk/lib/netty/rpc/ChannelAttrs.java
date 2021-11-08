package com.salk.lib.netty.rpc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import com.salk.lib.netty.rpc.protocol.ResponseFuture;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * {@link Channel}自定义属性
 */
public abstract class ChannelAttrs {
	// common attr
	final static AttributeKey<Map<Long, ResponseFuture>> attr_futureResponseMap = AttributeKey.valueOf("futureResponseMap");
	// client side attr
	final static AttributeKey<ServiceProvider> attr_serviceProvider = AttributeKey.valueOf("ServiceProvider");
	final static AttributeKey<ServiceProxy> attr_serviceProxy = AttributeKey.valueOf("ServiceProxy");
	// server side attr
	final static AttributeKey<Boolean> attr_isAuthorised = AttributeKey.valueOf("isAuthorised");

	/**
	 * 是否鉴权通过
	 * 
	 * @param channel
	 * @return
	 */
	public static boolean isAuthorised(Channel channel) {
		Boolean flag = channel.attr(attr_isAuthorised).get();
		return flag != null && flag;
	}


	/**
	 * 链路未完成请求Map(requestId : {@link Future})
	 * 
	 * @param channel
	 * @return
	 */
	public static Map<Long, ResponseFuture> getFutureResponseMap(Channel channel) {
		Map<Long, ResponseFuture> map = channel.attr(attr_futureResponseMap).get();
		if (map == null) {
			return getIfAbsentSet(channel, attr_futureResponseMap, new ConcurrentHashMap<Long, ResponseFuture>());
		} else {
			return map;
		}
	}

	/**
	 * 设置服务提供者信息
	 * 
	 * @param channel
	 * @param serviceProvider
	 */
	public static void setServiceProviderIfAbsent(Channel channel, ServiceProvider serviceProvider) {
		getIfAbsentSet(channel, attr_serviceProvider, serviceProvider);
	}


	/**
	 * 设置与此channel相关的 {@link ServiceProxy}
	 * 
	 * @param channel
	 * @param serviceProxy
	 */
	public static void setSeviceProxyIfAbsent(Channel channel, ServiceProxy serviceProxy) {
		getIfAbsentSet(channel, attr_serviceProxy, serviceProxy);
	}

	/**
	 * 获取与此channel相关的 {@link ServiceProxy}
	 * 
	 * @param channel
	 */
	public static ServiceProxy getServiceProxy(Channel channel) {
		return channel.attr(attr_serviceProxy).get();
	}

	/**
	 * 获取channel属性，如果不存在则用初始值设置
	 * 
	 * @param channel
	 * @param attr
	 * @param initValue
	 * @return
	 */
	static <A> A getIfAbsentSet(Channel channel, AttributeKey<A> attr, A initValue) {
		A value = channel.attr(attr).get();
		if (value == null) {
			synchronized (channel) {
				value = channel.attr(attr).get();
				if (value == null) {
					value = initValue;
					channel.attr(attr).set(initValue);
				}
			}
		}
		return value;
	}

}
