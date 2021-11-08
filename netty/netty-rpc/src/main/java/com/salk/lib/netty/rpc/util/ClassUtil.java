package com.salk.lib.netty.rpc.util;

import java.lang.reflect.Method;

/**
 * 类型工具类
 *
 */
public abstract class ClassUtil {

	@SuppressWarnings("unchecked")
	public static <T> T castFrom(Object o) {
		return (T) o;
	}

	/**
	 * Creates a unique mangled method name based on the method name and the
	 * method parameters.
	 * 
	 * @param method
	 *            the method to mangle
	 */
	public static String mangleMethodName(Method method) {
		Class<?>[] argTypes = method.getParameterTypes();
		String[] argTypeStrings = new String[argTypes.length];
		for (int i = 0; i < argTypeStrings.length; i++) {
			argTypeStrings[i] = argTypes[i].getName();
		}
		return mangleMethodName(method.getName(), argTypeStrings);
	}

	/**
	 * 返回由方法名和参数名组成的，唯一确定 {@link Method}的字符串
	 * 
	 * @param methodName
	 * @param argTypeStrings
	 * @return
	 */
	public static String mangleMethodName(String methodName, String[] argTypeStrings) {
		StringBuilder sb = new StringBuilder();

		sb.append(methodName);
		for (int i = 0; i < argTypeStrings.length; i++) {
			sb.append("_");
			sb.append(argTypeStrings[i]);
		}

		return sb.toString();
	}

}
