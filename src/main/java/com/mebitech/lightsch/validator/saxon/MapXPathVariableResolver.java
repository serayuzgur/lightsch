package com.mebitech.lightsch.validator.saxon;

import com.mebitech.lightsch.parser.element.Let;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathVariableResolver;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapXPathVariableResolver implements XPathVariableResolver {

	private Map<String,Object> vMap = new HashMap<>();

	public MapXPathVariableResolver(List<Let> lets){
		copyToMap(lets);
	}

	@Override
	public Object resolveVariable(QName variableName) {
		return vMap.get(variableName.toString());
	}

	public void addVariables(List<Let> lets) {
		copyToMap(lets);
	}

	private void copyToMap(List<Let> lets) {
		for(Let let: lets){
			vMap.put(let.getName(),let.getValue());
		}
	}
}
