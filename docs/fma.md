## fma

> To make programming and testing easy, I migrated the source code from CECore to CA-CDR-V2, 
and made changes to adapt the code with the generic feature model.
> After checking, if you agree with the changes in this branch, I will migrate back the new code of fma from CA-CDR-V2 to CECore.

### Changes compared to the last version

1. [**Explanators**](https://github.com/manleviet/CA-CDR-V2/tree/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/explanator), e.g., [**VoidFMExplanator**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/explanator/VoidFMExplanator.java), are now triggered by corresponding [**Analysis** classes](https://github.com/manleviet/CA-CDR-V2/tree/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/analysis), e.g., [**VoidFMAnalysis**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/analysis/VoidFMAnalysis.java).
2. [**FMAnalyzer**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/FMAnalyzer.java) manages a _list_ of [**AbstractFMAnalysis**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/analysis/AbstractFMAnalysis.java) instead of a _map_ between [**AbstractFMAnalysis**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/analysis/AbstractFMAnalysis.java) and [**AbstractAnomalyExplanator**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/explanator/AbstractAnomalyExplanator.java).
3. List of analyses in [**FMAnalyzer**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/FMAnalyzer.java) can be reset.
4. [**FMAnalyzer**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/FMAnalyzer.java) now supports to [monitor](https://github.com/manleviet/CA-CDR-V2/tree/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/monitor) the progress of analyses.
5. [**AnomalyType**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/anomaly/AnomalyType.java) conforms [**IAnomalyType**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/anomaly/IAnomalyType.java) to provide the extensibility of new analysis operations.
6. [**Builders**](https://github.com/manleviet/CA-CDR-V2/tree/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/builder), e.g., [**DeadFeatureAnalysisBuilder**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/builder/DeadFeatureAnalysisBuilder.java), help to generate analyses.
7. [**Explanations**](https://github.com/manleviet/CA-CDR-V2/tree/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/explanation), e.g., [**VoidFMExplanation**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/explanation/VoidFMExplanation.java), help to generate explanations.
8. [**AutomatedAnalysisBuilder**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/builder/AutomatedAnalysisBuilder.java) encapsulates all built-in builders.
9. [**AutomatedAnalysisExplanation**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/explanation/AutomatedAnalysisExplanation.java) encapsulates all built-in explanations.

### Simple usage of the package

The following example shows simple usage of the package:

```java
File fileFM = new File("src/test/resources/basic_featureide_redundant1.xml");

// 1. Create the factory for anomaly feature models
IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
        factory = FMParserFactory.getInstance(featureBuilder);

// 2. Create the parser
@Cleanup("dispose")
FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
        parser = factory.getParser(fileFM.getName());
// 3. Parse the feature model
FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
        featureModel = parser.parse(fileFM);

// 4. Create an analyzer
FMAnalyzer analyzer = new FMAnalyzer();

// 5. Select analysis operations to be performed
EnumSet<AnomalyType> options = EnumSet.of(AnomalyType.DEAD,
                                          AnomalyType.FULLMANDATORY,
                                          AnomalyType.REDUNDANT);

// 6. Generates analyses and add them to the analyzer
AutomatedAnalysisBuilder analysisBuilder = new AutomatedAnalysisBuilder();
analysisBuilder.build(featureModel, options, analyzer);

// 7. Run the analyzer
// (true - trigger the corresponding explanator if the assumption is violated)
analyzer.run(true);

// 8. Print the result
AutomatedAnalysisExplanation explanation = new AutomatedAnalysisExplanation();
System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), options));
```

If you want to perform all built-in analyses, you can replace the statement in Step 5 by the following statement:

```java
EnumSet<AnomalyType> options = EnumSet.allOf(AnomalyType.class);
```

### [**AutomatedAnalysisBuilder**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/builder/AutomatedAnalysisBuilder.java) and [**AutomatedAnalysisExplanation**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/explanation/AutomatedAnalysisExplanation.java)

These classes are utility classes that provides useful and shortcut ways to build built-in analyses 
and to explain the results of analyses.
However, you can also use directly specific builder and explanation classes to build and explain analyses.
The following example shows how to analysis dead features by using [**DeadFeatureAnalysisBuilder**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/builder/DeadFeatureAnalysisBuilder.java) and [**CompactExplanation**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/explanation/CompactExplanation.java):

```java
File fileFM = new File("src/test/resources/bamboobike_featureide_deadfeature1.xml");

// 1. Create the factory for anomaly feature models
IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
        factory = FMParserFactory.getInstance(featureBuilder);

// 2. Create the parser
@Cleanup("dispose")
FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
        parser = factory.getParser(fileFM.getName());
// 3. Parse the feature model
FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint>
        featureModel = parser.parse(fileFM);

// 4. Create an analyzer
FMAnalyzer analyzer = new FMAnalyzer();

// (omitted) 5. Select analysis operations to be performed

// 6. Generates analyses and add them to the analyzer (USING DeadFeatureAnalysisBuilder)
DeadFeatureAnalysisBuilder deadFeatureAnalysisBuilder = new DeadFeatureAnalysisBuilder();
deadFeatureAnalysisBuilder.build(featureModel, analyzer);

// 7. Run the analyzer
// (true - trigger the corresponding explanator if the assumption is violated)
analyzer.run(true);

// 8. Print the result (USING CompactExplanation)
CompactExplanation explanation = new CompactExplanation();
System.out.println(explanation.getDescriptiveExplanation(analyzer.getAnalyses(), DeadFeatureAnalysis.class, AnomalyType.DEAD));
```

> [**CompactExplanation**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/explanation/CompactExplanation.java) provides explanations for four analyses, including **DeadFeatureAnalysis**, **FalseOptionalAnalysis**,
> **FullMandatoryAnalysis**, and **ConditionallyDeadAnalysis**.

### How to monitor the progress of analysis

Before executing the analyzer, you can register a listener to monitor the progress of analysis.

```java
analyzer.setMonitor(new ProgressMonitor()); // MONITOR
// 7. Run the analyzer
analyzer.run(true);
```

In this way, the program will print the progress of analysis to the console.

_Example_: [testLargeModel_2](https://github.com/manleviet/CA-CDR-V2/blob/720fd21afa83caac8c2d8604632b7c595e6e8abe/fma/src/test/java/at/tugraz/ist/ase/fma/FMAnalyzerTest.java#L785)

### How to add a new analysis operation

1. Create a new enum type that conforms the interface [**IAnomalyType**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/anomaly/IAnomalyType.java).
2. Create a new "Assumptions" class that conforms the class [**IFMAnalysisAssumptionCreatable**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/assumption/IFMAnalysisAssumptionCreatable.java).
Assumptions classes are used to generate test cases for the analysis operation.
3. Create a new "Analysis" class that extends the class [**AbstractFMAnalysis**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/analysis/AbstractFMAnalysis.java).
Analysis classes are used to perform the analysis operation.
4. Create a new "Explanator" class that extends the class [**AbstractAnomalyExplanator**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/explanator/AbstractAnomalyExplanator.java).
Explanator classes are used to identify diagnoses for violated assumptions.
5. Create a new "Builder" class that conforms the class [**IAnalysisBuildable**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/builder/IAnalysisBuildable.java).
Builder classes are used to generate analyses.
6. Create a new "Explanation" class that conforms the class [**IAnalysisExplanation**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/fma/src/main/java/at/tugraz/ist/ase/fma/explanation/IAnalysisExplanable.java).
Explanation classes are used to generate explanations for the analysis results.
