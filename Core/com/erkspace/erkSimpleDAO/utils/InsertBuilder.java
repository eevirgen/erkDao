package com.erkspace.erkSimpleDAO.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InsertBuilder {

	public String build(Object model) {
		StringBuilder builder = new StringBuilder();
		String tableName = StringUtils.toAllCaps(model.getClass().getSimpleName());
		builder.append("INSERT INTO " + tableName + " (");
		int i = 0;
		List<Field> fields = Arrays.asList(model.getClass().getDeclaredFields());
		Collections.sort(fields, new FieldComparator());
		for (Field field : fields) {
			String name = StringUtils.toAllCaps(field.getName());
			if (i >= fields.size() - 1) {
				builder.append(", ");
			}
			builder.append(name);
			i += 1;
		}
		builder.append(") VALUES (");
		i = 0;
		for (int j = 0;  j < fields.size(); j++) {
			if (i >= fields.size() - 1) {
				builder.append(", ");
			}
			builder.append("?");
			i += 1;
		}
		builder.append(")");
		return builder.toString();
	}
}
