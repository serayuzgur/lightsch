package com.mebitech.lightsch.parser;

import com.mebitech.lightsch.parser.pojo.Schematron;
import org.junit.Test;

public class SchematronParserTest {

	@Test
	public void testParse() throws Exception {

		Schematron schematron = SchematronParser.parse(SchematronParserTest.class.getClassLoader().getResourceAsStream("edefter_unsigned_yevmiye.sch"));


		System.out.println(schematron.getSchema().getTitle());
		SchematronParser.parse(SchematronParserTest.class.getClassLoader().getResourceAsStream("universalTests.sch"));
	}
}