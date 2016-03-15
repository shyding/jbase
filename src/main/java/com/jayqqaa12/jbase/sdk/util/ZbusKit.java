package com.jayqqaa12.jbase.sdk.util;

import java.io.IOException;
import java.util.Map;

import org.zbus.broker.Broker;
import org.zbus.broker.BrokerConfig;
import org.zbus.broker.SingleBroker;
import org.zbus.rpc.RpcException;
import org.zbus.rpc.RpcInvoker;
import org.zbus.rpc.RpcProcessor;
import org.zbus.rpc.direct.Service;
import org.zbus.rpc.direct.ServiceConfig;

import com.google.common.collect.Maps;
import com.jayqqaa12.jbase.jfinal.ext.exception.JbaseRPCException;

public class ZbusKit {

	public static int DEFAULT_TIMEOUT = 15 * 1000;

	private static Map<String, Broker> brokes = Maps.newConcurrentMap();

	public static <T> T invokeSync(String key, Class<T> clazz, String method, Object... args) {

		return invokeSync(key, DEFAULT_TIMEOUT, clazz, method, args);
	}

	public static Broker getBroker(String addr) throws IOException {

		if (brokes.get(addr) == null) {
			BrokerConfig brokerConfig = new BrokerConfig();
			brokerConfig.setServerAddress(addr);
			Broker broke = new SingleBroker(brokerConfig);
			brokes.put(addr, broke);
			return broke;
		} else return brokes.get(addr);
	}

	/***
	 * 非 HA
	 */
	public static <T> T invokeSync(String addr, int timeout, Class<T> clazz, String method, Object... args) {

		try {
			
			RpcInvoker rpc = new RpcInvoker(getBroker(addr));
			rpc.setTimeout(timeout);
			return rpc.invokeSync(clazz, method, args);
			
		} catch (IOException e) {
			throw new JbaseRPCException(e);
		}

	}

	/**
	 * 启动 driect rpc 服务器 非 HA
	 * 
	 * 
	 * @param module
	 */
	public static void startDirectRpcService(Object... module) {

		try {
			ServiceConfig config = new ServiceConfig();
			// config.thriftServer = "0.0.0.0:25555";
			config.messageProcessor = new RpcProcessor(module);
			new Service(config).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
