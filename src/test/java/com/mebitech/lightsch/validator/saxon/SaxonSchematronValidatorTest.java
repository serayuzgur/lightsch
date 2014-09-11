package com.mebitech.lightsch.validator.saxon;

import com.mebitech.lightsch.parser.SchematronParser;
import com.mebitech.lightsch.parser.element.Assert;
import com.mebitech.lightsch.parser.element.Schematron;
import org.junit.Test;

import java.net.URL;
import java.util.List;

public class SaxonSchematronValidatorTest {

    @Test
    public void testValidate() throws Exception {
        Schematron schematron = SchematronParser.parse(SaxonSchematronValidatorTest.class.getClassLoader().getResourceAsStream("testSch.xml"));
        URL xmlPath = SaxonSchematronValidatorTest.class.getClassLoader().getResource("positiveData.xml");
        List<Assert> failed = SaxonSchematronValidator.validate(schematron, xmlPath);
        assert failed.isEmpty();


    }

    @Test
    public void testBreak() throws Exception {

        Schematron schematron = SchematronParser.parse(SaxonSchematronValidatorTest.class.getClassLoader().getResourceAsStream("testSch.xml"));
        URL xmlPath = SaxonSchematronValidatorTest.class.getClassLoader().getResource("negativeData.xml");
        List<Assert> failed = SaxonSchematronValidator.validate(schematron, xmlPath);

        System.out.println("\n\n*******************************************************************");
        System.out.println("Failed Size: " + failed.size());
        System.out.println("*******************************************************************\n\n");

        assert failed.size() == 84;

    }


}