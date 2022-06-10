# KBStatistics

A Knowledge Base Statistics Tool

| *version* | *status* |
|---|---|
| [1.3.1](https://github.com/manleviet/CA-CDR-V2/releases/tag/kbstatistics-v1.3.1)| latest |

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

### Usage

**Requirements**: OpenJDK 17.0.2

**Syntax**: 
```
java -jar kbstatistics.jar [-h] [-kb <PC>|<Renault>] [-fm <feature_model_name>] [-fm-dir <path_to_folder>] [-out <path_to_file>]
```

If the parameter `-out` isn't specified, the statistics results will be saved in the file naming `statistics.txt`.

**Examples**:
- Print out the help
`java -jar kbstatistics.jar -h`
- Saving the statistics of the PC knowledge base on the default file (`statistics.txt`)
`java -jar kbstatistics.jar -kb PC`
- Saving the statistics of the PC knowledge base on `pc_results.txt `
`java -jar kbstatistics.jar -kb PC -out ./pc_results.txt`
- Saving the statistics of the PC and Renault knowledge bases on the default file (`statistics.txt`)
`java -jar kbstatistics.jar -kb PC Renault`
- Saving the statistics of the smartwatch feature model on the default file (`statistics.txt`)
`java -jar kbstatistics.jar -fm smartwatch.sxfm`
- Saving the statistics of feature models in the folder `fms` on the default file (`statistics.txt`)
`java -jar kbstatistics.jar -fm-dir ./fms`
- Saving the statistics of PC, Renault, and feature models in the folder `fms` on the default file (`statistics.txt`)
`java -jar kbstatistics.jar -fm-dir ./fms -kb PC Renault`
