package com.mebitech.lightsch.parser.element;

import org.xml.sax.Attributes;

public class Namespace {
	private String prefix;
	private String uri;

	public Namespace(String prefix, String uri) {
		this.prefix = prefix;
		this.uri = uri;
	}

	public Namespace(Attributes attributes) {
		this(attributes.getValue("prefix"),attributes.getValue("uri"));
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}
