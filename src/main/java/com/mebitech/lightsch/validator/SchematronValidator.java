package com.mebitech.lightsch.validator;


import com.mebitech.lightsch.parser.pojo.*;
import com.ximpleware.*;

import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

public class SchematronValidator {


	public static String validate(Schematron schematron, URL xmlUrl) throws XPathParseException, NavException, XPathEvalException {

		VTDGen vg = new VTDGen();
		if (vg.parseFile(xmlUrl.getPath(), true)) {
			VTDNav vn = vg.getNav();
			AutoPilot ap = new AutoPilot();
			// Enter namespaces to xpath engine
			addDeclarations(schematron, ap);
			ap.bind(vn);
			//Traverse  patterns
			int i = 0;
			for (Pattern pattern : schematron.getSchema().getPatterns()) {

				//Traverse rules  and set context limitations
				for (Rule rule : pattern.getRules()) {

//					addLetDeclerations(schematron.getSchema(), rule, ap);
					//Test Asserts with replacing let variables.
					for (Assert anAssert : rule.getAsserts()) {
						System.out.println("--------------------------");

						replaceLetVariables(schematron.getSchema(),rule,anAssert);
						System.out.println(i++ + ": Test : " + anAssert.getTest());
						ap.selectXPath(anAssert.getTest());

						if (!isValidXPath(ap, vn)) {
							System.out.println("Context : " + rule.getContext());
							System.out.println(anAssert.getMessage());
						}
					}
				}


				ap.resetXPath();
			}


		}


		return "o";

	}

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

			int index = 0;
			int i = 0;
			AtomicInteger type = new AtomicInteger(); // 0 evalXpath 1 boolean 2 number 3 string
			type.set(-1);

			while (checkExp(ap, type)) {
				i++;
			}

			System.out.println("count : " + i);
			return i < 1;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static boolean checkExp(AutoPilot ap, AtomicInteger type) {
		int index = -1;
		switch (type.get()) {
			case -1:
				try {
					index = ap.evalXPath();
					type.set(0);
				} catch (XPathEvalException e) {
					index = ap.evalXPathToNumber() > 0 ? 1 : -1;
					type.set(1);
				} catch (NavException e) {
					ap.evalXPathToNumber();
				}
				break;
			case 0:
				try {
					index = ap.evalXPath();
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
