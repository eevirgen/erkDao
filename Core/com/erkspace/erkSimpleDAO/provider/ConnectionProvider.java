package com.erkspace.erkSimpleDAO.provider;

import java.sql.Connection;
import java.sql.SQLException;

import com.erkspace.erkSimpleDAO.builder.Configuration;

public interface ConnectionProvider {

	public Connection getConnection(Configuration configuration) throws SQLException;
}
