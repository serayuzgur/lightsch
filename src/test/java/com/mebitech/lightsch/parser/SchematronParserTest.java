package com.mebitech.lightsch.parser;

import com.mebitech.lightsch.parser.element.*;
import org.junit.Test;

import java.util.List;

public class SchematronParserTest {

    @Test
    public void testParse() throws Exception {
        int letCount = 0;
        Schematron schematron = SchematronParser.parse(SchematronParserTest.class.getClassLoader().getResourceAsStream("sch/edefter_unsigned_kebir.xml"));
        System.out.println(schematron.getSchema().getTitle());
        List<Let> glLet = schematron.getSchema().getLets();

        // Global 'let' variables
        for (Let let : glLet) {
            letCount++;
            System.out.println(letCount + ". \n Name : " + let.getName() + "\n" +
                    " Value : " + let.getValue());
        }


        // Local 'let' variables

        List<Pattern> patterns = schematron.getSchema().getPatterns();
        for (Pattern pattern : patterns) {
            List<Rule> rules = pattern.getRules();
            for (Rule rule : rules) {
                for (Assert anAssert : rule.getAsserts()) {
                    List<Let> lets = rule.getLets();
                    for (Let let : lets) {
                        XPathUtil.replaceLetVariables(schematron.getSchema(), rule, anAssert);
                        letCount++;
                        System.out.println(letCount + ". \n Name : " + let.getName() + "\n Value : " + let.getValue());
                    }
                }
            }
        }
        SchematronParser.parse(SchematronParserTest.class.getClassLoader().getResourceAsStream("sch/universalTests.xml"));

    }
}