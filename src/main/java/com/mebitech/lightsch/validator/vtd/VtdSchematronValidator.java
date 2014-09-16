package com.mebitech.lightsch.validator.vtd;


import com.mebitech.lightsch.parser.XPathUtil;
import com.mebitech.lightsch.parser.element.*;
import com.mebitech.lightsch.validator.SchematronValidationException;
import com.mebitech.lightsch.validator.SchematronValidator;
import com.ximpleware.*;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class VtdSchematronValidator extends SchematronValidator {
    private static final Logger LOGGER = Logger.getLogger(VtdSchematronValidator.class);

    private static void addDeclarations(Schematron schematron, AutoPilot ap) {
        for (Namespace namespace : schematron.getSchema().getNamespaces()) {
            ap.declareXPathNameSpace(namespace.getPrefix(), namespace.getUri());
        }
    }

    public List<Assert> validate(Schematron schematron, URL xmlUrl) throws SchematronValidationException, InterruptedException {

        List<Assert> asserts = new LinkedList<Assert>();
        VTDGen vg = new VTDGen();
        LOGGER.debug("Validating...");
        LOGGER.info("Parsing : " + xmlUrl.getPath());
        if (vg.parseFile(xmlUrl.getPath(), true)) {
            VTDNav vn = vg.getNav();
            AutoPilot ap = new AutoPilot();
            AutoPilot ap2 = new AutoPilot(vn);
            // Enter namespaces to xpath engine
            addDeclarations(schematron, ap);
            addDeclarations(schematron, ap2);
            ap.bind(vn);
            ap2.bind(vn);
            //Traverse  patterns
            int i = 0;
            LOGGER.info("Checking Started...");
            for (Pattern pattern : schematron.getSchema().getPatterns()) {
                //Traverse rules  and set context limitations
                for (Rule rule : pattern.getRules()) {
                    try {
                        ap.selectXPath(rule.getContext());
                    } catch (XPathParseException e) {
                        e.printStackTrace();
                    }
                    for (Assert anAssert : rule.getAsserts()) {
                        anAssert.setTest(XPathUtil.modifyXPath4Vtd(anAssert.getTest()));
                        XPathUtil.replaceLetVariables(schematron.getSchema(), rule, anAssert);
                        i++;
                        try {
                            ap2.selectXPath(anAssert.getTest());
                            List<Assert> asserts_ = doTest(ap, ap2, vn, anAssert);
                            asserts.addAll(asserts_);
                        } catch (XPathParseException e) {
                            e.printStackTrace();
                        }
                        ap.resetXPath();
                    }
                }
            }
            LOGGER.info("Checking Finished.");
        }
        return asserts;
    }

    /**
     * Doing all test with given location ( 'ap' determines the location and 'ap2' doing tests.)
     * First ap uses evalXpath method because it just takes you where you want to go.
     * Second ap (ap2) uses evalXpathToBoolean because it tries to learn is this expression valid or not ?
     *
     * @param ap       Visits each 'rule context' and waits for ap2 to finish his tests
     * @param ap2      Start its test from where ap1 brings him
     * @param vn       is just for taking xpath values for
     * @param anAssert meaningless, just for log and assert list, could be remove later
     * @return A list with full of errors.
     */
    private List<Assert> doTest(AutoPilot ap, AutoPilot ap2, VTDNav vn, Assert anAssert) {
        List<Assert> asserts = new LinkedList<Assert>();
        int index;
        int a = 0;
        LOGGER.info("Started : " + anAssert.getTest());
        try {
            while ((index = ap.evalXPath()) != -1) {
                if (!ap2.evalXPathToBoolean()) {
                    LOGGER.error(" \n******** \n Error Occured! Index : " + index + " Expression Value "
                            + vn.getXPathStringVal() + " Description  : " + anAssert.getMessage() + " \n********");
                    a++;
                    asserts.add(anAssert);
                }
            }
        } catch (XPathEvalException e) {
            e.printStackTrace();
        } catch (NavException e) {
            e.printStackTrace();
        }
        LOGGER.info("Error count : " + a);
        return asserts;
    }

    //
    /*
    Unused Code parts
     */
//    private static boolean isValidXPath(AutoPilot ap, AutoPilot ap2, VTDNav vn, Assert assert_) throws NavException {
//        try {
//            int i = 0;
//            AtomicInteger type = new AtomicInteger(); // 0 evalXpath 1 boolean 2 number 3 string
//            type.set(-1);
//            while (checkExp(ap, ap2, type, assert_, vn)) {
//                i++;
//            }
//            return i > 0;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    private static boolean checkExp(AutoPilot ap, AutoPilot ap2, AtomicInteger type, Assert assert_, VTDNav vn) throws XPathParseException, NavException, XPathEvalException {
//
//        int index = -1;
//        int a = 0;
//        switch (type.get()) {
//            case -1:
//                ap2.selectXPath(assert_.getTest());
//                LOGGER.info("Started : " + assert_.getTest());
//                while ((index = ap.evalXPath()) != -1) {
//                    if (!ap2.evalXPathToBoolean()) {
//                        a++;
////                        LOGGER.error(" \n******** \n Error Occured! Index : " + vn.getXPathStringVal() + " Description  : " + assert_.getMessage() + " \n********");
//                    }
//                }
//                LOGGER.info("error count " + a);
//                type.set(0);
//                return true;
//
//            case 0:
//                try {
//                    index = ap.evalXPath();
//                    return false;
//
//                } catch (XPathEvalException e) {
//                    e.printStackTrace();
//                    index = -1;
//                } catch (NavException e) {
//                    e.printStackTrace();
//                    index = -1;
//                }
//                break;
//
//            case 1:
//                index = -1;
//                break;
//
//        }
//
//        return index != -1;


//    }

//    }
//    private static void addLetDeclerations(Schema schema, Rule rule, AutoPilot ap) throws XPathParseException {
//        ap.clearVariableExprs();
//        for (Let let : schema.getLets()) {
//            ap.declareVariableExpr(let.getName(), let.getValue());
//        }
//        if (!rule.getLets().isEmpty()) {
//            for (Let let : rule.getLets()) {
//                ap.declareVariableExpr(let.getName(), let.getValue());
//            }
//        }
//    }
//
//    private static boolean hasXPath(AutoPilot ap) throws XPathEvalException, NavException {
//        return ap.evalXPath() != -1;
//    }
}
