package com.mebitech.lightsch.parser;


import com.mebitech.lightsch.parser.element.Schematron;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

public class SchematronParser {
	public static Schematron parse(InputStream schIs) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		SchematronHandler handler = new SchematronHandler();
		saxParser.parse(schIs,handler);
		return handler.getSchematron();
	}
}
