package com.mebitech.lightsch.parser.element;

import com.mebitech.lightsch.parser.XPathUtil;
import org.xml.sax.Attributes;

public class Assert {
	private String test;
	private String message;

	public Assert(Attributes attributes) {
		this(attributes.getValue("test"));
	}

	public Assert(String test) {
		this.test = test;
	}

	public String normalizeXPath(String test) {
		//Open with VTD
//		test = XPathUtil.convertMatchesToStringLength(test);
//		test = XPathUtil.removeFuncInPaths(test);
		test = XPathUtil.wrapWithNotFunc(test);
		return test;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
