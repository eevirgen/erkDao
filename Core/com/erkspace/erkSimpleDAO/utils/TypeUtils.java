package com.erkspace.erkSimpleDAO.utils;

import java.sql.Types;

public class TypeUtils {

	public static int toSqlType(Class<?> klass) {
		if (String.class.equals(klass)) {
			return Types.VARCHAR;
		} else if (int.class.equals(klass) || Integer.class.equals(klass)) {
			return Types.INTEGER;
		}
		return Types.VARCHAR;
	}
}
