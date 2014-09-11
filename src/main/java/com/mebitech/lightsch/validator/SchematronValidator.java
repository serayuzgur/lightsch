package com.mebitech.lightsch.validator;

import com.mebitech.lightsch.parser.element.Assert;
import com.mebitech.lightsch.parser.element.Rule;
import com.mebitech.lightsch.parser.element.Schematron;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.List;

public abstract class SchematronValidator {

	private static final Logger LOGGER = Logger.getLogger(SchematronValidator.class);
	public abstract List<Assert> validate(Schematron schematron, URL xmlUrl) throws SchematronValidationException;

	protected void assertFailedAction(List<Assert> asserts, int i, Assert anAssert) {
		LOGGER.error("Assert #" + i + " Failed: " + anAssert.getTest());
		LOGGER.error("\t" + anAssert.getMessage());
		asserts.add(anAssert);
	}

	protected void ruleFailedAction(List<Assert> asserts, Rule rule) {
		LOGGER.error("Rule " + rule.getContext() + " Failed ");
		asserts.addAll(rule.getAsserts());
	}
}
