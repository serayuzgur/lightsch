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

    /**
     * Takes all assert tests from schematron file and apply iteratively
     *
     * @param schematron lightSch schmetron object
     * @param xmlUrl     xml file to validate
     * @return list of failed asserts, each asserts hold index and offset values of the error
     * @throws SchematronValidationException
     * @throws InterruptedException
     * @throws XPathParseException
     * @throws NavException
     * @throws XPathEvalException
     */
    public List<Assert> validate(Schematron schematron, URL xmlUrl) throws SchematronValidationException, InterruptedException, NavException, XPathEvalException {

        List<Assert> asserts = new LinkedList<Assert>();
        VTDGen vg = new VTDGen();
        LOGGER.debug("Validating...");
        LOGGER.info("Parsing : " + xmlUrl.getPath());
        if (vg.parseFile(xmlUrl.getPath(), true)) {
            VTDNav vn = vg.getNav();
            AutoPilot ap = new AutoPilot(vn);
            AutoPilot ap2 = new AutoPilot(vn);
            // Enter namespaces to xpath engine
            addDeclarations(schematron, ap);
            addDeclarations(schematron, ap2);
            //Traverse  patterns
            LOGGER.info("Checking Started...");
            for (Pattern pattern : schematron.getSchema().getPatterns()) {
                LOGGER.debug("Pattern Name : " + pattern.getId());
                for (Rule rule : pattern.getRules()) {
                    LOGGER.debug("Evaluating Xpath Expression : " + rule.getContext());
                    try {
                        ap.selectXPath(rule.getContext());
                    } catch (XPathParseException e) {
                        asserts.add(new Assert(rule.getContext()));
                    }
                    for (Assert anAssert : rule.getAsserts()) {
                        XPathUtil.replaceLetVariables(schematron.getSchema(), rule, anAssert);
                        anAssert.setTest(XPathUtil.modifyXPath4Vtd(anAssert.getTest()));
                        List<Assert> asserts_ = doTest(ap, ap2, vn, anAssert);
                        asserts.addAll(asserts_);
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
    private List<Assert> doTest(AutoPilot ap, AutoPilot ap2, VTDNav vn, Assert anAssert) throws NavException, XPathEvalException {
        List<Assert> asserts = new LinkedList<Assert>();
        int index;
        // Just finds specified path and moves vn to there
        try {
            ap2.selectXPath(anAssert.getTest());
        } catch (XPathParseException e) {
            asserts.add(anAssert);
            LOGGER.error(e.getMessage());
            return asserts;
        }

        while ((index = ap.evalXPath()) != -1) {
            // tests given expression
            if (!ap2.evalXPathToBoolean()) {
                // Error occurred if we are here
                LOGGER.info("*******************************  ERROR XML ************************************");
                LOGGER.error(new String(vn.getElementFragmentNs().toBytes()));
                LOGGER.error(anAssert.getMessage());
                LOGGER.error(anAssert.getMessage());
                LOGGER.info("*********************************************************************************");
                anAssert.setElementFragment(vn.getElementFragment());
                anAssert.setIndex(index);
                asserts.add(anAssert);
            }
        }
        return asserts;
    }
}
