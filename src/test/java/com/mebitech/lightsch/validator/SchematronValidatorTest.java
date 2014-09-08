package com.mebitech.lightsch.validator;

import com.mebitech.lightsch.parser.SchematronParser;
import com.mebitech.lightsch.parser.pojo.Assert;
import com.mebitech.lightsch.parser.pojo.Schematron;
import org.junit.Test;

import java.net.URL;
import java.util.List;

public class SchematronValidatorTest {

    @Test
    public void testValidate() throws Exception {
        Schematron schematron = SchematronParser.parse(SchematronValidatorTest.class.getClassLoader().getResourceAsStream("testSch.xml"));
        URL xmlPath = SchematronValidatorTest.class.getClassLoader().getResource("positiveData.xml");
        List<Assert> failed = SchematronValidator.validate(schematron, xmlPath);
        assert failed.isEmpty();


    }

    @Test
    public void testBreak() throws Exception {

        Schematron schematron = SchematronParser.parse(SchematronValidatorTest.class.getClassLoader().getResourceAsStream("testSch.xml"));
        URL xmlPath = SchematronValidatorTest.class.getClassLoader().getResource("negativeData.xml");
        List<Assert> failed = SchematronValidator.validate(schematron, xmlPath);

        System.out.println("\n\n*******************************************************************");
        System.out.println("Failed Size: " + failed.size());
        System.out.println("*******************************************************************\n\n");

        assert failed.size() == 84;

    }


    @Test
    public void testPathFunctionRemover() {
        String xpath = "not(gl-cor:account/normalize-space(gl-cor:accountMainID)) or not(gl-cor:account/normalize-space(gl-cor:accountSub/gl-cor:accountSubID)) or starts-with(gl-cor:account/normalize-space(gl-cor:accountSub/gl-cor:accountSubID), gl-cor:account/normalize-space(gl-cor:accountMainID))";
        String replaced = "not(not(gl-cor:account/gl-cor:accountMainID) or not(gl-cor:account/gl-cor:accountSub/gl-cor:accountSubID) or starts-with(gl-cor:account/gl-cor:accountSub/gl-cor:accountSubID, gl-cor:account/gl-cor:accountMainID))";
        Assert anAssert = new Assert(xpath);

        System.out.println(replaced);
        System.out.println(anAssert.getTest());
        assert anAssert.getTest().equals(replaced);


    }
}