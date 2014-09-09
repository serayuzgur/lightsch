package com.mebitech.lightsch.parser;

public class XPathUtil {


	public static String removeFuncInPaths(String original) {
		String modified = original.replaceAll("/[a-z,A-Z]*\\:?[a-z,A-Z]+\\-?[a-z,A-Z]+\\(([^)]+)[\\)]{1}", "/$1");
		return modified;
	}

	public static String wrapWithNotFunc (String original){
		return "not(" + original + ")";
	}

	public static String convertMatchesToStringLength(String test) {
		if(!test.contains("matches"))
			return test;




		return test;
	}
}
