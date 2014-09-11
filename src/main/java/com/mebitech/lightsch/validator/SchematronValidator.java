package com.mebitech.lightsch.validator;

import com.mebitech.lightsch.parser.element.Assert;
import com.mebitech.lightsch.parser.element.Schematron;

import java.net.URL;
import java.util.List;

public interface SchematronValidator {

	public List<Assert> validate(Schematron schematron, URL xmlUrl) throws SchematronValidationException;
}
