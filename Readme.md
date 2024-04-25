A Tree-Based Compliance Checking Library
========================================

This project defines a library to evaluate the compliance of one or more event
traces with respect to a property expressed in Linear Temporal Logic. In
addition to the classical pass/fail verdict, the library can also distinguish
between different levels of satisfaction or violation of the property, by
comparing *evaluation trees*. These are structures induced by the evaluation
of a logical expression on an event trace. The library also allows users to
draw these trees and save them as image files.

Moreover, if a set of traces is passed to the tool, a diagram of the relation
between these traces can also be computed and drawn --this is called the
*Hasse diagram* of the relation.

The code contained in this repository provides an implementation and a
benchmark for the concepts described in the following research paper:

    S. Hallé. (2024). A Tree-Based Definition of Process Compliance.
    Submitted to EDOC 2024.

Project structure
-----------------

The repository is made of three separate projects, all contained in the
`Source` folder:

- `Core` contains the implementation of the library itself. Building this
  project using Ant generates a library (JAR file) that can be used to
  evaluate compliance on event logs (see command line instructions below).
- `Examples` is a project showing examples of compliance properties that can be
  evaluated using the tool. It requires the library created by the `Core`
  project.
- `Benchmark` contains an instance of a
  [LabPal](https://liflab.github.io/labpal) laboratory to evaluate the
  performance of the tool on various properties and logs. It also requires
  the `Core` library. More information about the benchmark can be found in its
  own Readme file.


Build instructions
------------------

To compile the tool (i.e. the `Core` project), make sure you have the
following:

- The Java Development Kit (JDK) to compile. The palette complies
  with Java version 8; it is probably safe to use any later version.
- [Ant](http://ant.apache.org) to automate the compilation and build process

At the command line, in the `Source`folder, simply typing

    ant

should take care of downloading all dependencies and compiling all three
projects. Otherwise, each project can be built separately by typing `ant`
in their respective folders.

Dependencies
------------

The palette requires the following Java libraries:

- The latest version of the [Synthia](https://liflab.github.io/synthia) data
  structure generator
- The latest version of the *Core*
  [Petit Poucet](https://github.com/liflab/petitpoucet) library and its
  *Functions* extension
- The latest version of the [Bullwinkle](https://github.com/sylvainhalle/Bullwinkle)
  parser
- The latest version of the [lif-fs](https://github.com/liflab/lif-fs)
  filesystem library
- The latest version of the [xml-lif](https://github.com/liflab/xml-lif)
  XML parsing library

These dependencies can be automatically downloaded and placed in the
`dep` folder of the project by typing:

    ant download-deps

The `Benchmark` project requires yet more libraries, which are documented in
the file `config.xml` of this specific folder.

Command line instructions
-------------------------

The tool can be used at the command line as a stand-alone application, using
the library `tc-1.0.jar`. The basic syntax is:

    java -jar tc-1.0.jar action --property file [options] file1 [file2 ...]

The `--property` parameter is mandatory and `file` should point to a text file
containing an LTL formula. The list of files `file1`, `file2`, etc. should
point to event traces in CSV or XML format (one trace per file).

Possible values for the parameter `action`:

- `compare`: evaluates the subsumption relation by comparing all pairs of
  `file1`, `file2`, etc. Prints an n x n matrix where entry (i,j) is 1 if
  trace i is subsumed by trace j and 0 otherwise.
- `draw-trees`: draws the evaluation tree of all traces given as an argument.
  By convention, each image filename is identical to that of the corresponding
  input trace, with the original extension replaced by `png`.
- `draw-hasse`: draws the Hasse diagram of the set of traces given as an
  argument. The `--output` parameter is mandatory and its argument specifies
  the name of the output image file.

<!-- :maxLineLen=78: -->