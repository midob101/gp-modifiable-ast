# General Purpose Modifiable AST Parser

This Java library is capable of generating an AST from a given grammar definition file.
The generated AST retains all tokens that are not part of the grammar, for example whitespaces.

The AST can be modified and source code can be generated based on the AST.

The main purpose of this library is to allow easier refactorings.

Currently, the only grammars beeing defined are for MiniJava, a subset of Java, and for 
a slightly extended MiniJava.

This project is part of a bachelors thesis, which can be found here: https://github.com/midob101/gp-thesis/blob/main/thesis.pdf.

Please refer to the bachelors thesis for a documentation about the project.

The library is distributed by a `jar` file in the github releases.