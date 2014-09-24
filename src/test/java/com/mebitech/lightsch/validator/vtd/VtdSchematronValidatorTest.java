package com.mebitech.lightsch.validator.vtd;

import com.mebitech.lightsch.parser.SchematronParser;
import com.mebitech.lightsch.parser.element.Assert;
import com.mebitech.lightsch.parser.element.Schematron;
import org.junit.Test;

import java.net.URL;
import java.util.List;

public class VtdSchematronValidatorTest {

    //TODO: Saxon will be handle in revivision 2
//    @Test
//    public void testValidate() throws Exception {
//        Schematron schematron = SchematronParser.parse(VtdSchematronValidatorTest.class.getClassLoader().getResourceAsStream("sch/testSch.xml"));
//        URL xmlPath = VtdSchematronValidatorTest.class.getClassLoader().getResource("sample/positiveBigData.xml");
//        List<Assert> failed = new VtdSchematronValidator().validate(schematron, xmlPath);
//        assert failed.isEmpty();
//    }

    @Test
    public void testBreak() throws Exception {

        long start = System.currentTimeMillis();
        Schematron schematron = SchematronParser.parse(VtdSchematronValidatorTest.class.getClassLoader().getResourceAsStream("sch/edefter_berat.xml"));
        URL xmlPath = VtdSchematronValidatorTest.class.getClassLoader().getResource("sample/kebir_berat.xml");
        List<Assert> failed = new VtdSchematronValidator().validate(schematron, xmlPath);
        long end = System.currentTimeMillis();


        System.out.println("\n\n*******************************************************************");
        System.out.println("Failed Size: " + failed.size() + " finished in : " + (end - start) + " miliseconds ");
        System.out.println("*******************************************************************\n\n");
        for (Assert assert4 : failed) {
            System.out.println("Message  : " + assert4.getMessage() + " Test : " + assert4.getTest() + " Index : " + assert4.getIndex() + " Element Fragment Index :  " + assert4.getElementFragment());
        }

    }

}