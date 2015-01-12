package com.erkspace.erkSimpleDAO.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UpdateBuilder {

	public String build(Object model) throws IllegalArgumentException, IllegalAccessException {
		StringBuilder builder = new StringBuilder();
		String tableName = StringUtils.toAllCaps(model.getClass().getSimpleName());
		builder.append("UPDATE " + tableName + " SET ");
		List<Field> fields = Arrays.asList(model.getClass().getDeclaredFields());
		Collections.sort(fields, new FieldComparator());
		int i = 0;
		for (Field field : fields) {
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			Object value = field.get(model);
			if (value == null) {
				continue;
			}
			if (i >= fields.size() - 1) {
				builder.append(", ");
			}
			String name = StringUtils.toAllCaps(field.getName());
			builder.append(name + " = ?");
			i += 1;
		}
		return builder.toString();
	}
}
