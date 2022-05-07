# Synthesized Feature Model Generator

This tool generates synthesized feature models using the Betty framework. For further details on Betty framework, we refer to [https://www.isa.us.es/betty/welcome](https://www.isa.us.es/betty/welcome).

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

### Dependencies

- [ChocoKB v1.2.7](https://github.com/manleviet/ChocoKB)
- [fm v1.2.9](https://github.com/manleviet/FeatureModelPackage)
- [common v1.2.4](https://github.com/manleviet/CommonPackage)
