# KBStatistics

A Knowledge Base Statistics Tool

### Features

The tool prints out statistics of given knowledge bases, which are as the following:

1. **General statistics**
  - The knowledge base name
  - The knowledge base source
  - Number of variables
  - Number of constraints
  - Number of Choco variables
  - Number of Choco constraints
  - The consistency of the knowledge base

2. **Statistics for feature model**
  - The CTC ratio
  - The number of features
  - The number of relationships
  - The number of cross-tree constraints
  - The number of MANDATORY relationships
  - The number of OPTIONAL relationships
  - The number of ALTERNATIVE relationships
  - The number of OR relationships
  - The number of REQUIRES constraints
  - The number of EXCLUDES constraints

### Supports the following knowledge bases

- _Feature Models_ from SPLOT, FeatureIDE, Glencoe, and other tools. You can find some feature model examples in [here](https://github.com/manleviet/KBStatistics/tree/main/src/test/resources/fms).
- _PC_ and _Renault_ from https://www.itu.dk/research/cla/externals/clib/

### Dependencies

- [ChocoKB v1.2.11](https://github.com/manleviet/ChocoKB)
- [fm v1.2.9](https://github.com/manleviet/FeatureModelPackage)
- [common v1.2.4](https://github.com/manleviet/CommonPackage)
