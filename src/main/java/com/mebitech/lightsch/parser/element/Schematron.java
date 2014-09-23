package com.mebitech.lightsch.parser.element;

public class Schematron  {
	private Schema schema;

	public Schema getSchema() {
		return schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}

    @Override
    public String toString() {
        return "Schematron{" +
                "schema=" + schema +
                '}';
    }
}
