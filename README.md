lightsch
========

Fast, Light Schematron Validator.

Motivation
----------
Our main motivation is to create a library which is faster and lighter than its competitors. Nowadays It is incredible hard and slow to work with large XML and SCH files. According to us a good validator must have some main features to work with all scenarios.

These main features are,

1. Stream validation must be supported. Most of the open source libraries only support whole document validation.
2. It should be able to work with SCH files directly. Any convert operation to XSL is unnecessary.
3. Output of validator must be well structured and interchangeable. (XML,JSON,CSV...)

Benchmarks
----------
TODO: Test system and library benchmarks will be documented here.


License
----------
License will maybe change later according to the helper libs. But be sure that it will be free as a bird.


Status
----------
* NOTE: **It's far beyond working beware of this code** !!!!
* Schematron parsing is done with  SAX parser and a **custom handler**. So very limited tag set is working for now. And we are planning to limit it as much as we can.
Here is the list,
  * schema
  * ns
  * title - Maybe we will remove this from data structure.
  * let
  * pattern
  * rule
  * assert

