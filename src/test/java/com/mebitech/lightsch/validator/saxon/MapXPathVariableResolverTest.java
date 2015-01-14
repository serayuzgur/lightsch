package com.mebitech.lightsch.validator.saxon;

import com.mebitech.lightsch.parser.element.Let;
import org.junit.Test;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

public class MapXPathVariableResolverTest {
	List<Let> list = Arrays.asList(
			new Let("a", "A"),
			new Let("b", "B")
	);
	List<Let> list2 = Arrays.asList(
			new Let("c", "C")
	);
	MapXPathVariableResolver resolver = new MapXPathVariableResolver(list);
	@Test
	public void testResolveVariable() throws Exception {
		assert resolver.resolveVariable(QName.valueOf("a")).equals("A");
	}

	@Test
	public void testAddVariables() throws Exception {
		resolver.addVariables(list2);
		assert resolver.resolveVariable(QName.valueOf("c")).equals("C");
	}
}