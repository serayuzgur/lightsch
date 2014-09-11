lightsch
========

Fast, Light Schematron Validator. We use SAX Parser for parsing [Schematron](http://http://www.schematron.com/) and [VTD-XML](http://http://vtd-xml.sourceforge.net/) for processing XPATH expressions.

Motivation
----------
Our main motivation is to create a library which is faster and lighter than its competitors. Nowadays It is incredible hard and slow to work with large XML and SCH files. According to us a good validator must have some main features to work with all scenarios. 

These main features are,

1. Stream validation must be supported. Most of the open source libraries only support whole document validation. **(NOT YET)**
2. It should be able to work with SCH files directly. Any convert operation to XSL is unnecessary. **(DONE)**
3. Output of validator must be well structured and interchangeable. (XML,JSON,CSV...) **(Gives list of objects , convertable to anything. So DONE )**

Benchmarks
----------
TODO: Test system and library benchmarks will be documented here.


License
----------
License will maybe change later according to the helper libs. But be sure that it will be free as a bird.
    

Status
----------
* Tests are broken **We will fix them after we decide XPath Processor.**
* Added SAXON validator for XPath2.0 support
* VTD-XML Hacks.
  * Variable declarations on VTD-XML is  (maybe) not working so we put a string replacer for *<let>* declarations. __*(see: com.mebitech.lightsch.validator.vtd.VtdSchematronValidator.replaceLetVariables())*__
  * Some limitations on VTD-XML on XPATH-2.0 forces us to make some filter on expressions (Not Sure).
    * Functions in xpath node queries are node supported. **ex. /a/b/normalize-space(c) It will be converted to /a/b/c** __*(see: com.mebitech.lightsch.parser.XpathUtils)*__
    * **matches** function is not working. Asserts which use this func. automatically eliminated by validator. 
* Schematron parsing is done with  SAX parser and a **custom handler**. So very limited tag set is working for now. And we are planning to limit it as much as we can.
Here is the list,
  * schema
  * ns
  * title - Maybe we will remove this from data structure.
  * let
  * pattern
  * rule
  * assert

