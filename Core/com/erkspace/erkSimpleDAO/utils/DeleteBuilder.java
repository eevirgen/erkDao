package com.erkspace.erkSimpleDAO.utils;

public class DeleteBuilder {

	public String build(Class<?> klass, String criteria) {
		StringBuilder builder = new StringBuilder();
		builder.append("DELETE FROM " + klass.getSimpleName() + " WHERE " + criteria);
		return builder.toString();
	}
}
