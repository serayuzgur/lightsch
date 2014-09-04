package com.mebitech.lightsch.validator;

import com.mebitech.lightsch.parser.SchematronParser;
import com.mebitech.lightsch.parser.pojo.Schematron;
import org.junit.Test;

import java.net.URL;

public class SchematronValidatorTest {

	@Test
	public void testValidate() throws Exception {
		//TODO: Write a real test

		Schematron schematron = SchematronParser.parse(SchematronValidatorTest.class.getClassLoader().getResourceAsStream("edefter_unsigned_yevmiye.sch"));
		URL xmlPath = SchematronValidatorTest.class.getClassLoader().getResource("2131233211-201012-YUS-0001.xml");

		for (int i = 0; i < 5; i++) {
			long start = System.currentTimeMillis();
			try {

				SchematronValidator.validate(schematron, xmlPath);

			} catch (Exception e){
				e.printStackTrace();
			}finally {
				start = System.currentTimeMillis() - start;
				System.out.println("Time : " + start);
			}
		}

	}
}