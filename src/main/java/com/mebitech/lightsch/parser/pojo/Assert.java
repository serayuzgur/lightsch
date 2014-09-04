package com.mebitech.lightsch.parser.pojo;

import org.xml.sax.Attributes;

public class Assert {
	private String test;
	private String message;

	public Assert(Attributes attributes) {
		//TODO: functions in xpath node queries are node supported. ex. /a/b/normalize-space(c)
		test = attributes.getValue("test");
		test = test.replaceAll("/[a-z,A-Z]*\\:?+[a-z,A-Z]+\\-?[a-z,A-Z]+\\(([^)]+)[\\)]{1}","/$1");

		test = "not(" + test + ")";
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
