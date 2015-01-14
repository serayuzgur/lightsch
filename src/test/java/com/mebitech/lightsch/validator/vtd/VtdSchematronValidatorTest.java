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
        Schematron schematron = SchematronParser.parse(VtdSchematronValidatorTest.class.getClassLoader().getResourceAsStream("sch/testSch.xml"));
        URL xmlPath = VtdSchematronValidatorTest.class.getClassLoader().getResource("sample/positiveData.xml");
        List<Assert> asserts = new VtdSchematronValidator().validate(schematron, xmlPath);

        for (Assert anAssert : asserts) {
            assert (anAssert.getErrElementFragmentList().isEmpty());
        }
    }

    @Test
    public void testBreak() throws Exception {

        long start = System.currentTimeMillis();
        Schematron schematron = SchematronParser.parse(VtdSchematronValidatorTest.class.getClassLoader().getResourceAsStream("sch/testSch.xml"));
        URL xmlPath = VtdSchematronValidatorTest.class.getClassLoader().getResource("sample/negativeData.xml");
        List<Assert> asserts = new VtdSchematronValidator().validate(schematron, xmlPath);
        long end = System.currentTimeMillis();
        for (Assert anAssert : asserts) {
            List<Long> errLocations = anAssert.getErrElementFragmentList();
            if (!errLocations.isEmpty()) {
                System.out.println("******************************************************");
                System.out.println("Failed assert : " + anAssert.getTest() + "\n Error locations : " + errLocations.toString());
                System.out.println("******************************************************");
            }
        }
        System.out.println("Finished in : " + (end - start));

    }

}