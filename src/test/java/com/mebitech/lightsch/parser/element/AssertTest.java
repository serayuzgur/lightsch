package com.mebitech.lightsch.parser.element;

import org.junit.Test;

public class AssertTest {

	@Test
	public void testNormalizeXPath() throws Exception {
		String xpath = "not(gl-cor:account/normalize-space(gl-cor:accountMainID)) or not(gl-cor:account/normalize-space(gl-cor:accountSub/gl-cor:accountSubID)) or starts-with(gl-cor:account/normalize-space(gl-cor:accountSub/gl-cor:accountSubID), gl-cor:account/normalize-space(gl-cor:accountMainID))";
		String replaced = "not(not(gl-cor:account/gl-cor:accountMainID) or not(gl-cor:account/gl-cor:accountSub/gl-cor:accountSubID) or starts-with(gl-cor:account/gl-cor:accountSub/gl-cor:accountSubID, gl-cor:account/gl-cor:accountMainID))";
		Assert anAssert = new Assert(xpath);

		assert anAssert.normalizeXPath(xpath).equals(replaced);

	}
}