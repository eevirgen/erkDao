package com.erkspace.erkSimpleDAO.utils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.erkspace.erkSimpleDAO.builder.Configuration;
import com.erkspace.erkSimpleDAO.observer.ExecutionDate;
import com.erkspace.erkSimpleDAO.observer.ExecutionDateObserver;
import com.erkspace.erkSimpleDAO.provider.ConnectionProvider;

public class QueryHandler {

	ExecutionDate executionDate = new ExecutionDate();
	
	private ConnectionProvider provider;

	private Configuration configuration;

	public QueryHandler(ConnectionProvider provider, Configuration configuration) {
		this.provider = provider;
		this.configuration = configuration;
		new ExecutionDateObserver(executionDate);
	}

	protected void insert(Object model, String query) throws Exception {
		List<Field> fields = Arrays.asList(model.getClass().getDeclaredFields());
		Collections.sort(fields, new FieldComparator());
		try (Connection conn = provider.getConnection(configuration)) {
			try (PreparedStatement pstmt = conn.prepareStatement(query)) {
				int i = 1;
				for (Field field : fields) {
					if (!field.isAccessible()) {
						field.setAccessible(true);
					}
					Object value = field.get(model);
					if (value != null) {
						int type = TypeUtils.toSqlType(field.getDeclaringClass());
						pstmt.setObject(i, value, type);
						i += 1;
					}
				}
				pstmt.executeUpdate();
				executionDate.setDate(new Date());
			}
		}
	}

	protected void delete(String query) throws SQLException {
		try (Connection conn = provider.getConnection(configuration)) {
			try (Statement stmt = conn.createStatement()) {
				stmt.executeUpdate(query);
				executionDate.setDate(new Date());
			}
		}
	}

	protected void update(Object model, String query)
					throws SQLException, IllegalArgumentException, IllegalAccessException {
		List<Field> fields = Arrays.asList(model.getClass().getDeclaredFields());
		Collections.sort(fields, new FieldComparator());
		try (Connection conn = provider.getConnection(configuration)) {
			try (PreparedStatement pstmt = conn.prepareStatement(query)) {
				int i = 1;
				for (Field field : fields) {
					if (!field.isAccessible()) {
						field.setAccessible(true);
					}
					Object value = field.get(model);
					if (value != null) {
						int type = TypeUtils.toSqlType(field.getDeclaringClass());
						pstmt.setObject(i, value, type);
						i += 1;
					}
				}
				pstmt.executeUpdate();
				executionDate.setDate(new Date());
			}
		}
	}
}
