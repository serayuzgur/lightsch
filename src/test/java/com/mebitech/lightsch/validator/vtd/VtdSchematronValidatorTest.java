package com.mebitech.lightsch.validator.vtd;

import com.mebitech.lightsch.parser.SchematronParser;
import com.mebitech.lightsch.parser.element.Assert;
import com.mebitech.lightsch.parser.element.Schematron;
import org.junit.Test;

import java.net.URL;
import java.util.List;

public class VtdSchematronValidatorTest {

    @Test
    public void testValidate() throws Exception {
        Schematron schematron = SchematronParser.parse(VtdSchematronValidatorTest.class.getClassLoader().getResourceAsStream("testSch.xml"));
        URL xmlPath = VtdSchematronValidatorTest.class.getClassLoader().getResource("positiveBigData.xml");
        List<Assert> failed = new VtdSchematronValidator().validate(schematron, xmlPath);
        assert failed.isEmpty();


    }

    @Test
    public void testBreak() throws Exception {

        Schematron schematron = SchematronParser.parse(VtdSchematronValidatorTest.class.getClassLoader().getResourceAsStream("testSch.xml"));
        URL xmlPath = VtdSchematronValidatorTest.class.getClassLoader().getResource("positiveData.xml");
        List<Assert> failed = new VtdSchematronValidator().validate(schematron, xmlPath);

        System.out.println("\n\n*******************************************************************");
        System.out.println("Failed Size: " + failed.size());
        System.out.println("*******************************************************************\n\n");

        assert failed.size() == 84;

    }


}