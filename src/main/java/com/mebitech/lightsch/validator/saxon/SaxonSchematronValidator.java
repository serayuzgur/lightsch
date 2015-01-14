package com.mebitech.lightsch.validator.saxon;

import com.mebitech.lightsch.parser.XPathUtil;
import com.mebitech.lightsch.parser.element.Assert;
import com.mebitech.lightsch.parser.element.Pattern;
import com.mebitech.lightsch.parser.element.Rule;
import com.mebitech.lightsch.parser.element.Schematron;
import com.mebitech.lightsch.validator.SchematronValidationException;
import com.mebitech.lightsch.validator.SchematronValidator;
import net.sf.saxon.lib.NamespaceConstant;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

import javax.xml.transform.sax.SAXSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class SaxonSchematronValidator extends SchematronValidator {
    private static final Logger LOGGER = Logger.getLogger(SaxonSchematronValidator.class);
    public static final String OBJECT_MODEL_SAXON = "http://saxon.sf.net/jaxp/xpath/om";

    static {
        System.setProperty("javax.xml.xpath.XPathFactory:" + NamespaceConstant.OBJECT_MODEL_SAXON, "net.sf.saxon.xpath.XPathFactoryImpl");
    }


    public List<Assert> validate(Schematron schematron, URL xmlUrl) throws SchematronValidationException {
        List<Assert> asserts = new LinkedList<Assert>();
        LOGGER.debug("Validating...");
        LOGGER.info("Parsing : " + xmlUrl.getPath());
        try {
            InputSource is = new InputSource(xmlUrl.getPath());
            SAXSource docSrc = new SAXSource(is);

            XPathFactory factory = XPathFactory.newInstance(NamespaceConstant.OBJECT_MODEL_SAXON);

            XPath xpath = factory.newXPath();


            MapNamespaceContext namespaceContext = new MapNamespaceContext(schematron.getSchema().getNamespaces());
            xpath.setNamespaceContext(namespaceContext);

            LOGGER.info("Checking Started...");
            int i = 0;
            for (Pattern pattern : schematron.getSchema().getPatterns()) {
                for (Rule rule : pattern.getRules()) {
                    Object context = docSrc;
                    if (!rule.getContext().isEmpty())
                        context = xpath.evaluate(rule.getContext(), docSrc, XPathConstants.NODE);

                    for (Assert anAssert : rule.getAsserts()) {
                        XPathUtil.replaceLetVariables(schematron.getSchema(), rule, anAssert);
                        String result;
                        try {
                            result = xpath.evaluate(anAssert.getTest(), context);
                            if (result.equals("false") || "".equals(result))
                                assertFailedAction(asserts, ++i, anAssert);
                        } catch (XPathExpressionException e) {
                            e.printStackTrace();
                            asserts.add(anAssert);
                        }
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        LOGGER.info("Checking Finished.");
        return asserts;
    }

}
