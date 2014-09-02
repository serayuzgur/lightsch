package com.mebitech.lightsch.parser.pojo;

import org.xml.sax.Attributes;

public class Let {
	private String name;
	private String value;

	public Let(Attributes attributes) {
		this(attributes.getValue("name"),attributes.getValue("value"));
	}

	public Let(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
