# Synthesized Feature Model Generator

This tool generates synthesized feature models using the Betty framework. For further details on Betty framework, we refer to [https://www.isa.us.es/betty/welcome](https://www.isa.us.es/betty/welcome).

| *version* | *status* |
|---|---|
| [1.3](https://github.com/manleviet/CA-CDR-V2/releases/tag/fm-gen-v1.3) | stable |

### Parameters

1. The number of constraints (`-c`)
3. The number of generated feature models (`-fm`)
4. The cross tree constraints ratio (`-ctc`)
5. The maximum of generations, used in the evolutionary generator (`-g`)
6. The folder where generated feature models will be saved (`-out`)

### How it works

The number of features in each generated feature model is specified by the following formula:
```
numFeatures = numConstraints * 3 / 4;
```

In case of numFeatures < 10, the tool uses the Random generation. Otherwise, it uses an Evolutionary generator.

Generated feature models are saved using the [SPLOT format](http://www.splot-research.org).

### Usage

**Requirements**: OpenJDK 17.0.2

**Syntax**: 
```
java -jar fm_gen.jar [-h] -c <#constraints> -fm <#generated_feature_models> [-ctc <ratio_cross_tree_constraints>] [-g <#max_generations>] [-out <path_to_folder>]
```

If the parameters `-out`, `-ctc` and `-g` aren't specified, the default values are "./", "0.8" and "5" respectively.
