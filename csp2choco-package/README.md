# csp2choco-package

A translator, using the ANTLR4 runtime for parsing, enables converting CSP constraints into Choco Solver commands.

For further details about the library, please refer to the [Documentation](https://hiconfit.manleviet.info).

## Features

- Translate simple constraints in the form of __[variable]__ __[comparative op]__ __[variable/integer]__
- Support IntVar variables (either positive or negative)
- Support comparative operators: =, !=, >, >=, <, <=