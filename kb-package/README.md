# kb-package

A Maven package for Configuration Knowledge Bases in Choco Solver.

This package provides classes managing CSP (Choco) representations of a knowledge base.

For further details about the library, please refer to the [Documentation].

## Features

- Provides an abstract **KB** class managing variables, variable domains, and constraints of a knowledge base/feature model
- Provides the following Configuration Knowledge Bases:
  1. **FMKB** - an implementation for the CSP representation of feature models. The input of **FMKB** is a **FeatureModel** object (from [fm-package]). So you can use **FMKB** for all feature models you have, i.e., no need to implement a specific **KB** class.
  2. **PCKB** - an implementation of [PC Configuration Knowledge Base].
  3. **RenaultKB** - an implementation of [Renault Configuration Knowledge Base].
- Provides utility functions for constraints

[Documentation]: https://hiconfit.manleviet.info
[PC Configuration Knowledge Base]: https://www.itu.dk/research/cla/externals/clib/
[Renault Configuration Knowledge Base]: https://www.itu.dk/research/cla/externals/clib/
[fm-package]: https://github.com/HiConfiT/hiconfit-core/tree/main/fm-package