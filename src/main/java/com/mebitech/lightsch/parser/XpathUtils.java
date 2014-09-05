package com.mebitech.lightsch.parser;

public class XpathUtils {


	public static String removeFuncInPaths(String original) {
		String modified = original.replaceAll("/[a-z,A-Z]*\\:?[a-z,A-Z]+\\-?[a-z,A-Z]+\\(([^)]+)[\\)]{1}", "/$1");
		return modified;
	}

	public static String wrapWithNotFunc (String original){
		return "not(" + original + ")";
	}
}
