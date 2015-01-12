package com.erkspace.erkSimpleDAO.provider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.erkspace.erkSimpleDAO.builder.Configuration;

public class ConnectionProviderDefaultImplementation implements ConnectionProvider {

	@Override
	public Connection getConnection(Configuration configuration)
			throws SQLException {
		return DriverManager.getConnection(configuration.getUrl(), configuration.getUser(), configuration.getPassword());
	}
}
