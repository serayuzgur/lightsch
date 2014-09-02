package com.mebitech.lightsch.parser.pojo;

import java.util.LinkedList;
import java.util.List;

public class Scope {
	private String title;
	private List<Let> lets = new LinkedList<Let>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Let> getLets() {
		return lets;
	}

	public boolean addLet(Let let){
		return lets.add(let);
	}
}
