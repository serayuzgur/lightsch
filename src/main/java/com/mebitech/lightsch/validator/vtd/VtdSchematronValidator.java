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


	public List<Assert> validate(Schematron schematron, URL xmlUrl) throws SchematronValidationException {

		List<Assert> asserts = new LinkedList<Assert>();
		VTDGen vg = new VTDGen();
		LOGGER.debug("Validating...");
		LOGGER.info("Parsing : " + xmlUrl.getPath());
		if (vg.parseFile(xmlUrl.getPath(), true)) {
			VTDNav vn = vg.getNav();
			AutoPilot ap = new AutoPilot();
			// Enter namespaces to xpath engine
			addDeclarations(schematron, ap);
			ap.bind(vn);
			//Traverse  patterns
			int i = 0;
			LOGGER.info("Checking Started...");
			for (Pattern pattern : schematron.getSchema().getPatterns()) {

				//Traverse rules  and set context limitations
				for (Rule rule : pattern.getRules()) {
					// Check rule context;
					try {
						ap.selectXPath(rule.getContext());
					} catch (XPathParseException e) {
						e.printStackTrace();
						ruleFailedAction(asserts, rule);
						ap.resetXPath();
					}
					if (!checkContext(ap)) {
						ruleFailedAction(asserts, rule);
						ap.resetXPath();
					} else {
						for (Assert anAssert : rule.getAsserts()) {
							anAssert.setTest(XPathUtil.modifyXPath4Vtd(anAssert.getTest()));
							XPathUtil.replaceLetVariables(schematron.getSchema(), rule, anAssert);
							i++;
							try {
								ap.selectXPath(anAssert.getTest());
							} catch (XPathParseException e) {
								e.printStackTrace();
								assertFailedAction(asserts, i, anAssert);
							}
							try {
								if (!isValidXPath(ap, vn)) {
									assertFailedAction(asserts, i, anAssert);
								}
							} catch (NavException e) {
								e.printStackTrace();
								assertFailedAction(asserts, i, anAssert);
							}
						}
					}

				}


				ap.resetXPath();
			}
			LOGGER.info("Checking Finished.");
		}


		return asserts;

	}

	private static void addLetDeclerations(Schema schema, Rule rule, AutoPilot ap) throws XPathParseException {
		ap.clearVariableExprs();
		for (Let let : schema.getLets()) {
			ap.declareVariableExpr(let.getName(), let.getValue());
		}
		if (!rule.getLets().isEmpty()) {
			for (Let let : rule.getLets()) {
				ap.declareVariableExpr(let.getName(), let.getValue());
			}
		}
	}

	private static boolean hasXPath(AutoPilot ap) throws XPathEvalException, NavException {
		return ap.evalXPath() != -1;
	}

	private static boolean isValidXPath(AutoPilot ap, VTDNav vn) throws NavException {
		try {
			int i = 0;
			AtomicInteger type = new AtomicInteger(); // 0 evalXpath 1 boolean 2 number 3 string
			type.set(-1);

			while (checkExp(ap, type)) {
				i++;
			}
			return i > 0;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static boolean checkContext(AutoPilot ap) {
		int index = -1;
		try {
			index = ap.evalXPath();
		} catch (XPathEvalException e) {
			e.printStackTrace();
			return false;
		} catch (NavException e) {
			e.printStackTrace();
			return false;
		}
		return index > -1;
	}

	private static boolean checkExp(AutoPilot ap, AtomicInteger type) {

		int index = -1;
		switch (type.get()) {
			case -1:
				try {
					index = ap.evalXPath();
					type.set(0);
					return index == -1;
				} catch (XPathEvalException e) {
					index = ap.evalXPathToNumber() > 0 ? -1 : 1;
					type.set(1);
				} catch (NavException e) {
					ap.evalXPathToNumber();
				}
				break;

			case 0:
				try {
					index = ap.evalXPath();
					return false;

				} catch (XPathEvalException e) {
					e.printStackTrace();
					index = -1;
				} catch (NavException e) {
					e.printStackTrace();
					index = -1;
				}
				break;

			case 1:
				index = -1;
				break;

		}

		return index != -1;
	}


	private static void addDeclarations(Schematron schematron, AutoPilot ap) {
		for (Namespace namespace : schematron.getSchema().getNamespaces()) {
			ap.declareXPathNameSpace(namespace.getPrefix(), namespace.getUri());
		}
	}
}