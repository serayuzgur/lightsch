package com.mebitech.lightsch.parser.element;

import com.mebitech.lightsch.parser.XPathUtil;
import org.xml.sax.Attributes;

public class Assert {
    private String test;
    private String message;
    private int index;
    // long representation of location of the xml elemnt
    // first 32 bit is starting offset and second 32 bit is length of the element
    private long elementFragment;

    public Assert(Attributes attributes) {
        this(attributes.getValue("test"));
    }

    public Assert(String test) {
        this.test = test;
    }

    public String normalizeXPath(String test) {
        //Open with VTD
//		test = XPathUtil.convertMatchesToStringLength(test);
//		test = XPathUtil.removeFuncInPaths(test);
        test = XPathUtil.wrapWithNotFunc(test);
        return test;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getElementFragment() {
        return elementFragment;
    }

    public void setElementFragment(long elementFragment) {
        this.elementFragment = elementFragment;
    }
}
