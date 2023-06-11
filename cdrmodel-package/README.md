# cdrmodel-package

This package provides a programmatic approach to manage/prepare the input constraints/test cases for [consistency-based algorithms](https://github.com/manleviet/CA-CDR-V2/tree/main/ca-cdr-package).

For further details about the library, please refer to the [Documentation].

## Features

- An abstract class CDRModel managing a set of constraints which we assume to be always correct and another set of constraints that could be faulty
- Some TestModels used by unit-tests of [consistency-based algorithms](https://github.com/manleviet/CA-CDR-V2/tree/main/ca-cdr-package).
- FMDebuggingModel - an extension of the CDRModel for debugging tasks of feature models, i.e., working with [test cases](https://github.com/manleviet/CA-CDR-V2/tree/main/test-package).
- Test case and test suite management
- Test case and test suite reading
- Translation of test cases into Choco constraints

[Documentation]: https://hiconfit.manleviet.info