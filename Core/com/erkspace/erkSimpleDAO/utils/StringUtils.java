package com.erkspace.erkSimpleDAO.utils;

import java.util.Locale;

public class StringUtils {

	public static String toAllCaps(String str) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (Character.isUpperCase(c) && i > 0) {
				builder.append("_");
			}
			builder.append(c);
		}
		return builder.toString().toUpperCase(Locale.ENGLISH);
	}
}
