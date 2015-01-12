package com.erkspace.erkSimpleDAO.utils;

import java.lang.reflect.Field;
import java.util.Comparator;

public class FieldComparator implements Comparator<Field> {

	@Override
	public int compare(Field o1, Field o2) {
		return o1.getName().compareTo(o2.getName());
	}
}
