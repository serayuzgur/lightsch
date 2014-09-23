package com.mebitech.lightsch.parser.element;

import com.mebitech.lightsch.parser.XPathUtil;
import org.xml.sax.Attributes;

public class Let {
    private String name;
    private String value;

    public Let(Attributes attributes) {
        this(attributes.getValue("name"), attributes.getValue("value"));
    }

    public Let(String name, String value) {
        this.name = name;
        this.value = XPathUtil.removeFuncInPaths(value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "\n" + "Let{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}' + "\n";
    }
}
