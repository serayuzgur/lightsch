package com.mebitech.lightsch.parser.pojo;

import com.mebitech.lightsch.parser.XpathUtils;
import org.xml.sax.Attributes;

public class Assert {
	private String test;
	private String message;

	public Assert(Attributes attributes) {
		this(attributes.getValue("test"));
	}

	public Assert(String test) {
		//TODO: functions in xpath node queries are node supported. ex. /a/b/normalize-space(c)
		test = XpathUtils.removeFuncInPaths(test);
		test = XpathUtils.wrapWithNotFunc(test);
		this.test = test;
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
