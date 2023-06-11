# fma-package

The library provides a mechanism to automatically generate property-based test cases for feature models 
and allows the automated determination of faulty constraints in the feature model.

For further details about the library, please refer to the [Documentation].

## Features

- New type of features (AnomalyAwareFeature) that can store information of anomalies 
- New type of test cases that are aware of assumptions (anomalies to be detected)
- 6 types of assumptions corresponding to 6 types of anomalies
- 6 analysis for validating assumptions
- Different analysis builders for 6 analysis
- 5 explanator for determining explanations
- A feature model analysis engine (FMAnalyzer) for analyzing feature models
- A mechanism to monitor the analysis process

[Documentation]: https://hiconfit.manleviet.info