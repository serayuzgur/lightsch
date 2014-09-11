package com.mebitech.lightsch.parser;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Arrays;
import java.util.regex.Pattern;

public class XPathUtil {


	public static String removeFuncInPaths(String original) {
		String modified = original.replaceAll("/[a-z,A-Z]*\\:?[a-z,A-Z]+\\-?[a-z,A-Z]+\\(([^)]+)[\\)]{1}", "/$1");
		return modified;
	}

	public static String wrapWithNotFunc (String original){
		return "not(" + original + ")";
	}

	public static String convertMatchesToStringLength(String test) {

        if (test.contains("matches")) {

            int indexMatches = test.indexOf("matches");
            int firstBracketIndex = test.indexOf("(", indexMatches);
            int lastBracketIndex = test.lastIndexOf(")");

            String matchesContext = test.substring(firstBracketIndex + 1, lastBracketIndex);
            String normalizeSpace = matchesContext.split(" , ")[0];
            String regex = matchesContext.split(" , ")[1];

            if (regex.contains("[0-9]")) {
                int firstParenthesis = regex.indexOf("{");
                int lastParenthesis = regex.indexOf("}", firstParenthesis);

                String lengthRange[] = regex.substring(firstParenthesis + 1, lastParenthesis).split(",");

                if (lengthRange.length > 1) {
                    String startOffset = lengthRange[0];
                    String endOffset = lengthRange[1];
                    //string-length(normalize-space(.)) >= 10 and string-length(normalize-space(.)) <= 11
                    test = "string-length" + "(" + normalizeSpace + ")" + " >= " + startOffset + " and " + "string-length" + "(" + normalizeSpace + ")" + " <= " + endOffset;
                } else {
                    String length = lengthRange[0];
                    test = "string-length" + "(" + normalizeSpace + ")" + " == " + length;
                }

            }

        }

        return test.trim();
    }


}
