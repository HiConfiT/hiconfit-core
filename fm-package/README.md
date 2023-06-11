# fm-package

This package provides the management functionalities for [basic feature models].

Please refer to the [Documentation] for further details about the library.

## Features

- Provides the generic FeatureModel class to construct a feature model with any type of features, relationships, and cross-tree constraints.
- Provides a feature model factory that can create a feature model parser that corresponds with the format of the given feature model
- Supports the following feature model formats:
    1. [SPLOT feature models]. The file extension could be “.sxfm” or “.splx.”
    2. [FeatureIDE format]. The file extension should be “xml.”
    3. v.control format. The feature model format of the v.control tool. The file extension should be “xmi.”
    4. [Glencoe format]. The file extension should be “json.”
    5. [Descriptive format]. Our feature model format. The file extension should be “fm4conf”.

[Documentation]: https://hiconfit.manleviet.info
[basic feature models]: https://apps.dtic.mil/sti/pdfs/ADA235785.pdf
[SPLOT feature models]: https://splot-research.org
[FeatureIDE format]: https://featureide.github.io
[Glencoe format]: https://glencoe.hochschule-trier.de
[Descriptive format]: https://github.com/HiConfiT/hiconfit-core/blob/main/fm-package/src/test/resources/bamboobike.fm4conf