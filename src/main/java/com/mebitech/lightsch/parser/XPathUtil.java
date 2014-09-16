package com.mebitech.lightsch.parser;

import com.mebitech.lightsch.parser.element.Assert;
import com.mebitech.lightsch.parser.element.Let;
import com.mebitech.lightsch.parser.element.Rule;
import com.mebitech.lightsch.parser.element.Schema;

public class XPathUtil {

    public static String modifyXPath4Vtd(String original) {
        // Matches function is not available in VTD so rewrite them
        original = XPathUtil.convertMatchesToStringLength(original);
        //Functions in xpath node queries are node supported. ex. /a/b/normalize-space(c)
        original = XPathUtil.removeFuncInPaths(original);
        return XPathUtil.wrapWithNotFunc(original);
    }

    public static String modifyXPath4Saxon(String original) {
        return XPathUtil.wrapWithNotFunc(original);
    }

    public static void replaceLetVariables(Schema schema, Rule rule, Assert anAssert) {
        String path = anAssert.getTest();
        for (Let let : schema.getLets()) {
            path = path.replaceAll("\\$" + let.getName(), let.getValue());
        }
        for (Let let : rule.getLets()) {
            path = path.replaceAll("\\$" + let.getName(), let.getValue());
        }

        anAssert.setTest(path);
    }

    public static String removeFuncInPaths(String original) {
        return original.replaceAll("/[a-z,A-Z]*\\:?[a-z,A-Z]+\\-?[a-z,A-Z]+\\(([^)]+)[\\)]{1}", "/$1");
    }

    public static String wrapWithNotFunc(String original) {
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
                    test = "string-length" + "(" + normalizeSpace + ")" + " = " + length;
                }

            }

        }

        return test.trim();
    }


}
