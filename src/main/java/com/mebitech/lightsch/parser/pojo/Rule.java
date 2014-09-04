package com.mebitech.lightsch.parser.pojo;

import java.util.LinkedList;
import java.util.List;

public class Rule extends Scope {
	private String context;
	private List<Assert> asserts = new LinkedList<Assert>();

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		if (context.equals("*"))
			context = "//" + context;
		this.context = context;
	}

	public List<Assert> getAsserts() {
		return asserts;
	}

	public boolean addAssert(Assert assert_) {

		if (!getContext().equals("/"))
			assert_.setTest(getContext() + "[" + assert_.getTest() + "]");

		//TODO: Remove after xpath2.0 support
		if (assert_.getTest().contains("matches"))
			return false;
		return asserts.add(assert_);
	}
}
