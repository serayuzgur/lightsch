package com.mebitech.lightsch.validator.saxon;

import com.mebitech.lightsch.parser.element.Namespace;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class MapNamespaceContextTest {

	List<Namespace> list = Arrays.asList(
			new Namespace("a", "A"),
			new Namespace("b", "B")
	);
	MapNamespaceContext context = new MapNamespaceContext(list);


	@Test
	public void testGetNamespaceURI() throws Exception {
		assert context.getNamespaceURI("a").equals("A") && context.getNamespaceURI("b").equals("B");

	}

	@Test
	public void testGetPrefix() throws Exception {
		assert context.getPrefix("A").equals("a") && context.getPrefix("B").equals("b");

	}

	@Test
	public void testGetPrefixes() throws Exception {
		assert context.getPrefixes("A").next().equals("a");

	}
}