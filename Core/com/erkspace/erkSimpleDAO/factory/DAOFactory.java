package com.erkspace.erkSimpleDAO.factory;

import static java.lang.reflect.Proxy.newProxyInstance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.erkspace.erkSimpleDAO.builder.Configuration;
import com.erkspace.erkSimpleDAO.provider.ConnectionProvider;
import com.erkspace.erkSimpleDAO.proxy.DAOProxy;

public class DAOFactory {

	// factories : It is a map which holds DAO instance which has been provided 
	// In this project, it is generally myDAO 
	private Map<Class<?>, Object> factories = new ConcurrentHashMap<>();

	private ConnectionProvider connectionProvider;

	private Configuration configuration;

	public DAOFactory(ConnectionProvider connectionProvider, Configuration configuration) {
		this.connectionProvider = connectionProvider;
		this.configuration = configuration;
	}

	@SuppressWarnings("unchecked")
	public <T> T getDAO(Class<T> klass) {
		if (!klass.isInterface()) {
			throw new RuntimeException("Class: " + klass.getName() + " must be interface");
		}
		if (factories.containsKey(klass)) {
			return (T) factories.get(klass);
		}
		Object proxy = createDAO(klass);
		factories.put(klass, proxy);
		return (T) proxy;
	}

	protected Object createDAO(Class<?> klass) {
		// Proxy instance has been created. To interception 
		DAOProxy proxy = new DAOProxy(connectionProvider, configuration);
		ClassLoader cl = getClass().getClassLoader();
		Object dao = newProxyInstance(cl, new Class[] { klass }, proxy);
		return dao;
	}
}
