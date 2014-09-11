package com.mebitech.lightsch.validator.saxon;

import com.mebitech.lightsch.parser.element.Namespace;

import javax.xml.namespace.NamespaceContext;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class MapNamespaceContext implements NamespaceContext {

	private Map<String,String> prefixMap = new HashMap<>();
	private Map<String,String> uriMap = new HashMap<>();

	public MapNamespaceContext(List<Namespace> namespaces){
		for(Namespace ns : namespaces){
			prefixMap.put(ns.getPrefix(), ns.getUri());
			uriMap.put(ns.getUri(),ns.getPrefix());
		}
	}
	@Override
	public String getNamespaceURI(String prefix) {
		return prefixMap.get(prefix);
	}

	@Override
	public String getPrefix(String namespaceURI) {
		return uriMap.get(namespaceURI);
	}

	@Override
	public Iterator getPrefixes(String namespaceURI) {
		return prefixMap.keySet().iterator();
	}
}
