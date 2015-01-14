package com.mebitech.lightsch.validator.saxon;

import com.mebitech.lightsch.parser.element.Namespace;

import javax.xml.namespace.NamespaceContext;
import java.util.*;


public class MapNamespaceContext implements NamespaceContext {

	private Map<String,String> prefixMap = new HashMap<String,String>();
	private Map<String,String> uriMap = new HashMap<String,String>();

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
		List<String> pfs = new LinkedList<String>();
		for(String pf : prefixMap.keySet()){
			if(prefixMap.get(pf).equals(namespaceURI))
				pfs.add(pf);
		}
		return pfs.iterator();
	}
}
