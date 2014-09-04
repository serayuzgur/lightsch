package com.mebitech.lightsch.parser.pojo;

import org.xml.sax.Attributes;

public class Assert {
	private String test;
	private String message;

	public Assert(Attributes attributes) {
		setTest(attributes.getValue("test"));
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		//TODO: functions in xpath node queries are node supported. ex. /a/b/normalize-space(c)
		test = test.replaceAll("/[a-z,A-Z]*\\:?+[a-z,A-Z]+\\-?[a-z,A-Z]+\\(([^)]+)[\\)]{1}","/$1");

		test = "not(" + test + ")";

		this.test = test;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
