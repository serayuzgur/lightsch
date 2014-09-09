package com.mebitech.lightsch.parser;

import com.mebitech.lightsch.parser.element.Schematron;
import org.junit.Test;

public class SchematronParserTest {

	@Test
	public void testParse() throws Exception {


		Schematron schematron = SchematronParser.parse(SchematronParserTest.class.getClassLoader().getResourceAsStream("testSch.xml"));


		System.out.println(schematron.getSchema().getTitle());
		SchematronParser.parse(SchematronParserTest.class.getClassLoader().getResourceAsStream("universalTests.xml"));
	}
}