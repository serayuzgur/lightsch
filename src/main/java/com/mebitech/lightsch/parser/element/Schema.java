package com.mebitech.lightsch.parser.element;

import java.util.LinkedList;
import java.util.List;

public class Schema extends Scope{
	private List<Namespace> namespaces = new LinkedList<Namespace>();
	private List<Pattern> patterns = new LinkedList<Pattern>();


	public List<Namespace> getNamespaces() {
		return namespaces;
	}

	public boolean addNamespace(Namespace namespace) {
		return namespaces.add(namespace);
	}

	public List<Pattern> getPatterns() {
		return patterns;
	}

	public boolean addPattern (Pattern pattern){
		return patterns.add(pattern);
	}
}
