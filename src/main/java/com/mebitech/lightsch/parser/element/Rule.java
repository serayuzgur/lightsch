package com.mebitech.lightsch.parser.element;

import java.util.LinkedList;
import java.util.List;

public class Rule extends Scope {
    private String context;
    private List<Assert> asserts = new LinkedList<Assert>();

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
//        if (context.equals("*"))
//            context = "//" + context;
        this.context = context;
    }

    public List<Assert> getAsserts() {
        return asserts;
    }

    public boolean addAssert(Assert assert_) {

//        if (!getContext().equals("/"))
//            assert_.setTest(assert_.getTest());

        return asserts.add(assert_);
    }

    @Override
    public String toString() {
        return "Rule{" +
                "context='" + context + '\'' +
                ", asserts=" + asserts +
                '}';
    }
}
