# kb-package

A Maven package for Configuration Knowledge Bases in Choco Solver.

This package includes classes that manage CSP (Choco) representations of a knowledge base.

Please refer to the [Documentation] for further details about the library.

## Features

- Provides an abstract **KB** class managing variables, variable domains, and constraints of a knowledge base/feature model
- Provides the following Configuration Knowledge Bases:
  1. **FMKB** - an implementation for the CSP representation of feature models. The input of **FMKB** is a **FeatureModel** object (from [fm-package]). So **FMKB** can be applied to all feature models without implementing a specific **KB** class.
  3. **PCKB** - an implementation of [PC Configuration Knowledge Base].
  4. **RenaultKB** - an implementation of [Renault Configuration Knowledge Base].
- Provides utility functions for constraints

[Documentation]: https://hiconfit.manleviet.info
[PC Configuration Knowledge Base]: https://www.itu.dk/research/cla/externals/clib/
[Renault Configuration Knowledge Base]: https://www.itu.dk/research/cla/externals/clib/
[fm-package]: https://github.com/HiConfiT/hiconfit-core/tree/main/fm-package