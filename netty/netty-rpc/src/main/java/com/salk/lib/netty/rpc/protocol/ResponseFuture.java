package com.salk.lib.netty.rpc.protocol;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import com.salk.lib.netty.rpc.Request;
import io.netty.channel.Channel;

/**
 * RPC响应异步结果
 * Created by 18073747 on 2018/7/25.
 *
 */
public final class ResponseFuture extends FutureTask<Response> {
	private final long startTime = System.currentTimeMillis();
	private final Channel channel;
	private final Request request;

	private static final Callable<Response> DEFAULT_CALLABLE = new Callable<Response>() {

		@Override
		public Response call() throws Exception {
			return null;
		}
	};

	public ResponseFuture() {
		this(DEFAULT_CALLABLE, null, null);
	}

	/**
	 * @return the channel
	 */
	public Channel getChannel() {
		return channel;
	}

	/**
	 * @return the request
	 */
	public Request getRequest() {
		return request;
	}

	public ResponseFuture(Callable<Response> callable, Channel channel, Request request) {
		super(callable);
		this.channel = channel;
		this.request = request;
	}

	@Override
	public void set(Response response) {
		super.set(response);
	}

	@Override
	public void setException(Throwable t) {
		super.setException(t);
	}

	public long getStartTime() {
		return startTime;
	}

}
