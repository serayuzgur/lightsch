package com.mebitech.lightsch.parser.element;

import com.mebitech.lightsch.parser.XPathUtil;
import org.xml.sax.Attributes;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Assert {
    private String test;
    private String message;
    // long representation of location of the xml elemnt
    // first 32 bit is starting offset and second 32 bit is length of the element
    private List<Long> errElementFragmentList = new LinkedList<Long>();

    public Assert(Attributes attributes) {
        this(attributes.getValue("test"));
    }

    public Assert(String test) {
        this.test = test;
    }

    public String normalizeXPath(String test) {
        //Open with VTD
//		test = XPathUtil.convertMatchesToStringLength(test);
        test = XPathUtil.removeFuncInPaths(test);
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


    public List<Long> getErrElementFragmentList() {
        return errElementFragmentList;
    }

    public void setErrElementFragmentList(List<Long> errElementFragmentList) {
        this.errElementFragmentList = errElementFragmentList;
    }

    public void addErrtoElementFragmentList(long elementFragment) {
        errElementFragmentList.add(elementFragment);
    }


    @Override
    public String toString() {
        return "Assert{" +
                "test='" + test + '\'' +
                ", message='" + message + '\'' +
                ", errElementFragmentList=" + errElementFragmentList +
                '}';
    }
}
