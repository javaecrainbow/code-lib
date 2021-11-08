package com.salk.lib.netty.rpc;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.salk.lib.netty.rpc.protocol.Response;
import com.salk.lib.netty.rpc.protocol.ResponseFuture;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public final class ServiceProxy implements InvocationHandler {
	private ServiceProxyContext serviceProxyContext;

	ServiceProxy() {
	}

	protected Object doFilter(Request request, Channel selectedChannel, ServiceProxyContext serviceProxyContext) throws Exception {
		// 采用nanoTime实现最精确地控制
		long nanoBeginTime = System.nanoTime();
		if (selectedChannel == null) {
			selectedChannel = selectChannelUnitDoneOrException(serviceProxyContext.getDirectedServiceProvider());
		}
		String mangleMethodName = request.getMangledMethodName();
		String interfaceName = request.getInterfaceName();
		// 序列化
		request.setBodyData(request.getSerializeType().getSerialization().serialize(request));
		//获取调用方法的超时时间
		try {
			long nanoTimeOut = MILLISECONDS.toNanos(serviceProxyContext.getServiceConsumerConfig().getMethodTimeOut(
					serviceProxyContext.getMethodName(mangleMethodName)));
			long timeWait = computeNanoTimeToFutureWait(nanoTimeOut, nanoBeginTime);
			while (timeWait > 0) {
				try {
					ResponseFuture responseFuture = sendRequest(selectedChannel, request);
					Response response = null;
					response = responseFuture.get(computeNanoTimeToFutureWait(nanoTimeOut, nanoBeginTime), TimeUnit.NANOSECONDS);
					Object rpcResult = response.getRpcResult();
					return rpcResult;
				}catch (TimeoutException e){
					break;
				}
			}
			throw new TimeoutException("time out");
		}catch (Exception e){
			throw e;
		}
	}

	/**
	 * 计算futuretask需要等待的时间
	 * @param nanoTimeOut
	 * @param nanoBeginTime
	 * @return
	 */
	private long computeNanoTimeToFutureWait(long nanoTimeOut, long nanoBeginTime) {
		return nanoTimeOut - (System.nanoTime() - nanoBeginTime);
	}

	/**
	 * Fast-Fail 挑选链路
	 */
	Channel selectChannelUnitDoneOrException(ServiceProvider serviceProvider) throws Exception {
		List<ServiceProvider> excludedServiceProviders = new ArrayList<ServiceProvider>(2);
		Channel selectedChannel = null;
		while (true) {
			try {
				selectedChannel = serviceProxyContext.selectChannel(serviceProvider);
				return selectedChannel;
			} catch (Exception e) {
				excludedServiceProviders.add(serviceProvider);
			}
		}
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (serviceProxyContext.containsMethod(method)) {
			Request request = new Request(serviceProxyContext.getServiceConsumerConfig().getServiceInterfaceName(),
					serviceProxyContext.getMangleMethodName(method), args);
			return doFilter(request, null, serviceProxyContext);
		}
		throw new Exception("method[" + method.getName() + "] is not in service["
				+ serviceProxyContext.getServiceConsumerConfig().getServiceInterfaceName() + "]");
	}

	void receiveResponse(Response response, Channel channel) {
			// isRPC
			ResponseFuture future = deregisterHandingRequest(channel, response.getRequestId());
			if (future != null) {
				future.set(response);
			}
	}

	final static AttributeKey<Map<Long, ResponseFuture>> attr_futureResponseMap = AttributeKey.valueOf("futureResponseMap");


	static ResponseFuture deregisterHandingRequest(Channel channel, long requestId) {
		return getFutureResponseMap(channel).remove(requestId);
	}
	static void registerHandingRequest(Channel channel, long requestId, ResponseFuture future) {
		getFutureResponseMap(channel).put(requestId, future);
	}
	public static Map<Long, ResponseFuture> getFutureResponseMap(Channel channel) {
		Map<Long, ResponseFuture> map = channel.attr(attr_futureResponseMap).get();
		if (map == null) {
			return getIfAbsentSet(channel, attr_futureResponseMap, new ConcurrentHashMap<Long, ResponseFuture>());
		} else {
			return map;
		}
	}

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

	/**
	 * 发送 {@link Request}
	 *
	 * @param channel
	 * @param request
	 * @return 未来的响应 {@link ResponseFuture}
	 */
	ResponseFuture sendRequest(final Channel channel, final Request request) {
		ResponseFuture future = new ResponseFuture();
		registerHandingRequest(channel, request.getRequestId(), future);
		channel.writeAndFlush(request, channel.voidPromise());
		return future;
	}
	public ServiceProxyContext getServiceProxyContext() {
		return serviceProxyContext;
	}

	public void setServiceProxyContext(ServiceProxyContext serviceProxyContext) {
		this.serviceProxyContext = serviceProxyContext;
	}
}
