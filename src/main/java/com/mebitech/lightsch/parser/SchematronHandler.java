package com.mebitech.lightsch.parser;

import com.mebitech.lightsch.parser.pojo.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Locale;

public class SchematronHandler extends DefaultHandler {
	private Schematron schematron;
	private boolean atSchema = false;
	private boolean atTitle = false;
	private Pattern currentPattern;
	private Rule currentRule = null;
	private Assert currentAssert = null;

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		if (!atSchema && isEquals(qName, "schema")) {
			schematron = new Schematron();
			schematron.setSchema(new Schema());
			atSchema = true;
		} else if (atSchema && isEquals(qName, "ns")) {
			schematron.getSchema().addNamespace(new Namespace(attributes));
		} else if (atSchema && isEquals(qName, "title")) {
			atTitle = true;
		} else if (atSchema && isEquals(qName, "let")) {
			Let let = new Let(attributes);
			if (currentRule == null) {
				schematron.getSchema().addLet(let);
			} else {
				currentRule.addLet(let);
			}
		} else if (atSchema && isEquals(qName, "pattern")) {
			currentPattern = new Pattern();
			currentPattern.setId(attributes.getValue("id"));
			schematron.getSchema().addPattern(currentPattern);
		} else if (atSchema && currentPattern != null && isEquals(qName, "rule")) {
			currentRule = new Rule();
			currentRule.setContext(attributes.getValue("context"));
			currentPattern.addRule(currentRule);
		}else if (atSchema && currentRule != null && isEquals(qName, "assert")) {
			currentAssert = new Assert(attributes);
			currentRule.addAssert(currentAssert);
		}


	}

	private boolean isEquals(String a, String b) {
		return a.toLowerCase(Locale.ENGLISH).endsWith(b);
	}

	public void endElement(String uri, String localName,
	                       String qName) throws SAXException {

		if (atSchema && isEquals(qName, "pattern")) {
			currentPattern = null;
		} else if (atSchema && currentPattern != null && isEquals(qName, "rule")) {
			currentRule = null;
		} else if(atSchema && currentRule != null && isEquals(qName, "assert")){
			currentAssert  = null;
		}
	}

	public void characters(char ch[], int start, int length) throws SAXException {

		if (atSchema && atTitle) {
			schematron.getSchema().setTitle(new String(ch, start, length));
			atTitle = false;
		} else if(currentAssert != null){
			currentAssert.setMessage(new String(ch, start, length));
		}
	}

	public Schematron getSchematron() {
		return schematron;
	}

	public void setSchematron(Schematron schematron) {
		this.schematron = schematron;
	}
}
