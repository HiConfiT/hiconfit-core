# cdrmodel-package

This package provides a programmatic approach to manage/prepare the input constraints/test cases for [consistency-based algorithms].

For further details about the library, please refer to the [Documentation].

## Features

- An abstract class CDRModel managing a set of constraints which we assume to be always correct and another set of constraints that could be faulty
- Some TestModels used by unit-tests of [consistency-based algorithms].
- FMDebuggingModel - an extension of the CDRModel for debugging tasks of feature models, i.e., working with [test cases].
- Test case and test suite management
- Test case and test suite reading
- Translation of test cases into Choco constraints

[Documentation]: https://hiconfit.manleviet.info
[consistency-based algorithms]: https://github.com/HiConfiT/hiconfit-core/tree/main/ca-cdr-package
[test cases]: https://github.com/HiConfiT/hiconfit-core/tree/main/ca-cdr-core-package/src/main/java/at/tugraz/ist/ase/hiconfit/cacdr_core
