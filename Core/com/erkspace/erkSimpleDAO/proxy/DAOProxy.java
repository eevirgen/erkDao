package com.erkspace.erkSimpleDAO.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.erkspace.erkSimpleDAO.builder.Configuration;
import com.erkspace.erkSimpleDAO.provider.ConnectionProvider;
import com.erkspace.erkSimpleDAO.utils.DeleteBuilder;
import com.erkspace.erkSimpleDAO.utils.InsertBuilder;
import com.erkspace.erkSimpleDAO.utils.QueryHandler;
import com.erkspace.erkSimpleDAO.utils.UpdateBuilder;

public class DAOProxy extends QueryHandler implements InvocationHandler {

	private static final String INSERT = "insert";

	private static final String DELETE = "delete";

	private static final String UPDATE = "update";

	public DAOProxy(ConnectionProvider connectionProvider, Configuration configuration) {
		super(connectionProvider, configuration);
	}

	// We intercept when MYDao interface method calls. these methods runs JVM.
	// We have to know which execution we have to execute. So we get method name
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String name = method.getName();
		Object model = args[0];
		if (INSERT.equals(name)) {
			String query = new InsertBuilder().build(model);
			insert(model, query);
		} else if (DELETE.equals(name)) {
			String criteria = (String) args[1];
			Class<?> klass = (Class<?>) args[0];
			String query = new DeleteBuilder().build(klass, criteria);
			delete(query);
		} else if (UPDATE.equals(name)) {
			String criteria = (String) args[1];
			String query = new UpdateBuilder().build(model);
			update(model, query + " WHERE " + criteria);
		}
		return null;
	}
}
