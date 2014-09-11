package com.mebitech.lightsch.validator.saxon;

import com.mebitech.lightsch.parser.element.*;
import net.sf.saxon.lib.NamespaceConstant;
import net.sf.saxon.s9api.SaxonApiException;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactoryConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class SaxonSchematronValidator {
	private static final Logger LOGGER = Logger.getLogger(SaxonSchematronValidator.class);
	public static final String OBJECT_MODEL_SAXON = "http://saxon.sf.net/jaxp/xpath/om";


	private static void replaceLetVariables(Schema schema, Rule rule, Assert anAssert) {
		String path = anAssert.getTest();
		for (Let let : schema.getLets()) {
			path = path.replaceAll("\\$" + let.getName(), let.getValue());
		}
		for (Let let : rule.getLets()) {
			path = path.replaceAll("\\$" + let.getName(), let.getValue());
		}

		anAssert.setTest(path);
	}

	public static List<Assert> validate(Schematron schematron, URL xmlUrl) throws XPathFactoryConfigurationException, SaxonApiException, ParserConfigurationException, IOException, SAXException {
		List<Assert> asserts = new LinkedList<Assert>();
		LOGGER.debug("Validating...");
		LOGGER.info("Parsing : " + xmlUrl.getPath());

		InputSource source = new InputSource(xmlUrl.getPath());
		javax.xml.xpath.XPathFactory factory =
				javax.xml.xpath.XPathFactory.newInstance();
		javax.xml.xpath.XPath xpath = factory.newXPath();

		System.setProperty("javax.xml.xpath.XPathFactory:"
						+ NamespaceConstant.OBJECT_MODEL_SAXON,
				"net.sf.saxon.xpath.XPathFactoryImpl");

		MapNamespaceContext namespaceContext = new MapNamespaceContext(schematron.getSchema().getNamespaces());
		xpath.setNamespaceContext(namespaceContext);


		for (Pattern pattern : schematron.getSchema().getPatterns()) {
			for (Rule rule : pattern.getRules()) {
				for (Assert anAssert : rule.getAsserts()) {
					replaceLetVariables(schematron.getSchema(), rule, anAssert);
					String result = "false";
					try {
						result = xpath.evaluate(anAssert.getTest(), source);
						if (!result.equals(""))
							asserts.add(anAssert);
						;
					} catch (XPathExpressionException e) {
						e.printStackTrace();
						asserts.add(anAssert);
					}
				}
			}
		}

		return asserts;
	}


}
