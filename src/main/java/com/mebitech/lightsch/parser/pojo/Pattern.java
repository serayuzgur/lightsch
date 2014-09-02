package com.mebitech.lightsch.parser.pojo;

import java.util.LinkedList;
import java.util.List;

public class Pattern {
	private String id;
	List<Rule> rules = new LinkedList<Rule>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public boolean addRule(Rule rule) {
		return rules.add(rule);
	}
}
