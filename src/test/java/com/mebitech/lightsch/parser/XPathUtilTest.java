package com.mebitech.lightsch.parser;

import org.junit.Test;

public class XPathUtilTest {

	@Test
	public void testConvertMatchesToStringLength() throws Exception {
		String xpath = "matches(normalize-space(.) , '^[0-9]{10,11}$')";
		String replaced = "string-length(normalize-space(.)) >= 10 and string-length(normalize-space(.)) <= 11  ";
		assert XPathUtil.convertMatchesToStringLength(xpath).equals(replaced);

	}
	@Test
	public void testWrapWithNotFunc() throws Exception {
		String xpath = "not(xxx)";
		String replaced = "not(not(xxx))";

		assert XPathUtil.wrapWithNotFunc(xpath).equals(replaced);

	}


	@Test
	public void testRemoveFuncInPaths() {
		String xpath = "not(gl-cor:account/normalize-space(gl-cor:accountMainID)) or not(gl-cor:account/normalize-space(gl-cor:accountSub/gl-cor:accountSubID)) or starts-with(gl-cor:account/normalize-space(gl-cor:accountSub/gl-cor:accountSubID), gl-cor:account/normalize-space(gl-cor:accountMainID))";
		String replaced = "not(gl-cor:account/gl-cor:accountMainID) or not(gl-cor:account/gl-cor:accountSub/gl-cor:accountSubID) or starts-with(gl-cor:account/gl-cor:accountSub/gl-cor:accountSubID, gl-cor:account/gl-cor:accountMainID)";
		assert XPathUtil.removeFuncInPaths(xpath).equals(replaced);
	}
}