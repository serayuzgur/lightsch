package com.mebitech.lightsch.validator;


import com.mebitech.lightsch.parser.pojo.*;
import com.ximpleware.*;

import java.net.URL;

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
			for (Pattern pattern : schematron.getSchema().getPatterns()) {

				//Traverse rules  and set context limitations
				for (Rule rule : pattern.getRules()) {

					//Test Asserts with replacing let variables.
					for (Assert anAssert : rule.getAsserts()) {
						System.out.println("--------------------------");
						System.out.println("Test : " + anAssert.getTest());
//						ap.resetXPath();
						ap.selectXPath(anAssert.getTest());

						if (!isValidXPath(ap,vn)) {
							System.out.println("Context : " + rule.getContext());
							System.out.println(anAssert.getMessage());
							System.exit(0);
						}
					}
				}


				ap.resetXPath();
			}


		}


		return "o";

	}

	private static boolean hasXPath(AutoPilot ap) throws XPathEvalException, NavException {
		return ap.evalXPath() != -1;
	}

	private static boolean isValidXPath(AutoPilot ap, VTDNav vn) throws NavException {
		try {

			int index = 0;
			int i = 0;


//			while ((index = ap.evalXPath()) != -1) {
//				i++;
//				if (i % 100 == 0)
//					System.out.println(i);
//				if(!(ap.evalXPathToNumber()<0))
//					return false;
//			}
			while (ap.iterate()) {
				i++;
				System.out.println(i +" : " +  index);
//				System.out.println(ap.evalXPathToBoolean());
//				ap.iterate();

			}
			System.out.println("count : " + i);
			return i < 1;

		} catch (Exception e) {
			return false;
		}
//		return !ap.evalXPathToBoolean();


	}


	private static void addDeclarations(Schematron schematron, AutoPilot ap) {
		for (Namespace namespace : schematron.getSchema().getNamespaces()) {
			ap.declareXPathNameSpace(namespace.getPrefix(), namespace.getUri());
		}
	}
}
