## fm-package

| **groupID**    | **_at.tugraz.ist.ase_**  |
|----------------|--------------------------|
| **artifactID** | **_fm-v2_**              |
| **version**    | **_1.3.9-alpha-42_**     |

The package follows the following objectives:

1. Provides a _generic_ mechanism that helps to easily obtain custom feature models, e.g., attribute-based feature models,
cardinality-based feature models, anomaly-aware feature models, etc.
2. Base classes (e.g., Feature, AbstractRelationship, CTConstraint, etc.) are immutable.
Mutable versions of these classes should be implemented in other packages to keep this package clean.
3. Supports only to _basic_ feature models, i.e., feature models with four relationships (_mandatory_, _optional_, _or_, 
and _alternative_) and two cross-tree constraints (_requires_ and _excludes_).
Other types of feature models should be implemented in other packages to keep this package simple.
4. Supports to arbitrary constraints with complex operators (e.g., /\\, \\/, not, ->, <->).
5. Supports common feature model formats found in the literature, i.e., [SPLOT](splot-research.org), [FeatureIDE](https://featureide.github.io).

### Contents

- [How to read a feature model from a file](#how-to-read-a-feature-model-from-a-file)
- [How to encode a feature model](#how-to-encode-a-feature-model)
- [How to use the _**generic**_ feature model with your custom features, relationships, and constraints](#how-to-use-the-generic-feature-model-with-your-custom-features-relationships-and-constraints)
- [What does the package provide?](#what-does-the-package-provide)

### How to read a feature model from a file

The following code shows the **simplest way** to read a feature model from a file.
This way, the parser will use the [_built-in builders_](#built-in-builders) to create the feature model. 

```java
// create a parser for the given file
// getIntance returns a parser that uses the built-in builders
FMParserFactory<Feature, AbstractRelationship<Feature>, CTConstraint> factory = FMParserFactory.getInstance();
@Cleanup("dispose")
FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = factory.getParser("filename");

// parse the feature model file
FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> fm = parser.parse("path/to/file");
```

Another example shows a **complete way** to read a feature model from a given file.
First, we need to create _builders_ on the basis of the [_built-in builders_](#built-in-builders).
Then, we can use the builders to create an **FMParserFactory**. 
We can create a parser for a given feature model format with the factory.
Finally, we can use the parser to read the feature model from a file.
In this way, if you want to construct a feature model with your custom features, relationships, and constraints, 
you only need to provide your custom builders at the first step.

```java
// create builders for features, relationships, and constraints using built-in builders
IFeatureBuildable featureBuilder = new FeatureBuilder();
IConfRuleTranslatable ruleTranslator = new ConfRuleTranslator();
IRelationshipBuildable relationshipBuilder = new RelationshipBuilder(ruleTranslator);
IConstraintBuildable constraintBuilder = new ConstraintBuilder(ruleTranslator);

// create a parser for the given file
FMParserFactory<Feature, AbstractRelationship<Feature>, CTConstraint> factory = FMParserFactory.getInstance(featureBuilder, relationshipBuilder, constraintBuilder);
@Cleanup("dispose")
FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = factory.getParser("filename");

// parse the feature model file
FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> fm = parser.parse("path/to/file");
```

The below code shows an example in which we read an anomaly-aware feature model from a file.
Particularly, the feature builder is customized to create anomaly-aware features.

```java
// create a custom builder for features
IFeatureBuildable featureBuilder = new AnomalyAwareFeatureBuilder();
// create built-in builders for relationships and constraints
IConfRuleTranslatable ruleTranslator = new ConfRuleTranslator();
IRelationshipBuildable relationshipBuilder = new RelationshipBuilder(ruleTranslator);
IConstraintBuildable constraintBuilder = new ConstraintBuilder(ruleTranslator);

// create a parser for the given file
FMParserFactory<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> factory = FMParserFactory.getInstance(featureBuilder, relationshipBuilder, constraintBuilder);
@Cleanup("dispose")
FeatureModelParser<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> parser = factory.getParser("filename");

// parse the feature model file
FeatureModel<AnomalyAwareFeature, AbstractRelationship<AnomalyAwareFeature>, CTConstraint> fm = parser.parse("path/to/file");
```

#### Other examples

1. Unit tests of 5 parsers
2. **KBStatistics** (the function _processFM_)
3. **FMAnalyzerTest**

### How to encode a feature model

The following code shows how to build a feature model from scratch.

```java
// create builders for features, relationships, and constraints using built-in builders
IFeatureBuildable featureBuilder = new FeatureBuilder();
IConfRuleTranslatable ruleTranslator = new ConfRuleTranslator();
IRelationshipBuildable relationshipBuilder = new RelationshipBuilder(ruleTranslator);
IConstraintBuildable constraintBuilder = new ConstraintBuilder(ruleTranslator);

// create an object for the feature model
FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> fm = new FeatureModel<>("test", featureBuilder, relationshipBuilder, constraintBuilder);

// create the root feature
Feature root = fm.addRoot("survey", "survey");

// create other features
// the order of adding features should be in the breadth-first order
Feature pay = fm.addFeature("pay", "pay");
Feature ABtesting = fm.addFeature("ABtesting", "ABtesting");
Feature statistics = fm.addFeature("statistics", "statistics");
Feature qa = fm.addFeature("qa", "qa");
Feature license = fm.addFeature("license", "license");
Feature nonlicense = fm.addFeature("nonlicense", "nonlicense");
Feature multiplechoice = fm.addFeature("multiplechoice", "multiplechoice");
Feature singlechoice = fm.addFeature("singlechoice", "singlechoice");

// create relationships
fm.addMandatoryRelationship(root, pay);
fm.addOptionalRelationship(root, ABtesting);
fm.addMandatoryRelationship(root, statistics);
fm.addMandatoryRelationship(root, qa);
fm.addAlternativeRelationship(pay, List.of(license, nonlicense));
fm.addOrRelationship(qa, List.of(multiplechoice, singlechoice));
fm.addOptionalRelationship(ABtesting, statistics);

// create constraints
fm.addRequires(ABtesting, statistics);
fm.addExcludes(ABtesting, nonlicense);
fm.addExcludes(ABtesting, root);
```

### How to use the _**generic**_ feature model with your custom features, relationships, and constraints

There are two requirements to use the _**generic**_ feature model:

1. The custom features, relationships, and cross-tree constraints must be inherited from the base classes:
[**Feature**](), [**AbstractRelationship**](), and [**CTConstraint**]().
2. Custom feature, relationship, and constraint _builders_ must be provided to the feature model.
These classes are inherited from the base classes:
[**IFeatureBuildable**](), [**IRelationshipBuildable**](), and [**IConstraintBuildable**](). 

An example could be found in the **_fma_** package.
In the class [**FMAnalyzerTest**](), the function _testConditionallyDead_0_ shows how to use the _**generic**_ feature model 
to support anomaly-aware features.
In this context, two new classes are defined.
The class [**AnomalyAwareFeature**]() is inherited from the base class [**Feature**](), and
the class [**AnomalyAwareFeatureBuilder**]() is inherited from the base class [**IFeatureBuildable**]().

### What does the package provide?

#### Generic FeatureModel class

The package provides the generic [**FeatureModel**]() class to construct a feature model with any type of features, 
relationships, and cross-tree constraints. The class accepts three bounded type parameters:

1. [**Feature**]() and all its subclasses
2. [**AbstractRelationship**]() and all its subclasses
3. [**CTConstraint**]() and all its subclasses

Since the class is generic, i.e., the class doesn't know how to build features, relationships, and constraints,
it requires _builders_ to create features, relationships, and constraints.
The package includes [_built-in builders_](#built-in-builders) for _basic_ features, relationships and constraints.
These builders conform to the interfaces [**IFeatureBuildable**](), [**IRelationshipBuildable**]() and [**IConstraintBuildable**]().

Besides, the class provides the following data structures:

1. A list of features - features are stored in the breadth-first order - access via _getBfFeatures()_
2. A list of relationships - access via _getRelationships()_
3. A list of cross-tree constraints - access via _getConstraints()_
4. A feature model tree - access via the method _getRoot()_, which returns the tree's root.

#### Feature class

Besides the basic information of a feature, e.g., _name_, _id_, the [**Feature**]() class contains a connection
to the parent feature, and a list of relationships starting from it.
The structure allows for the construction of a feature model tree.

#### Generic classes for relationships

The package provides an abstract class [**AbstractRelationship**]() 
and four inherited classes [**MandatoryRelationship**](), [**OptionalRelationship**](), 
[**OrRelationship**](), and [**AlternativeRelationship**]() 
to represent relationships of a _basic_ feature model.
These classes are generic and accept features with the type of [**Feature**]() or [**Feature**]()'s subclasses.

Besides, each relationship contains a connection to the parent feature and a list of child features.

#### CTConstraint class

The [**CTConstraint**]() class represents a cross-tree constraint of a feature model.
By supporting Abstract Syntax Trees (ASTs), the class allows the representation of complex constraints,
which are supported by the common feature model formats.

#### Configuration rule translator

When creating/initializing a relationship or a cross-tree constraint, a configuration rule translator
is required to translate the relationship or constraint into a text-based configuration rule.
For example, the built-in translator [**ConfRuleTranslator**]() translates a mandatory relationship
into the configuration rule _"mandatory(parent, child)"_.

The [**AbstractRelationship**]() and [**CTConstraint**]() classes accept custom translators,
if they conform to the interface [**IConfRuleTranslatable**]().

#### Feature, Relationship and Constraint Builders

The [**FeatureModel**]() class is generic and doesn't know how to build features, relationships, and constraints.
Therefore, the class requires _builders_ that are able to create features, relationships, and constraints.
These builders need to conform to the interfaces [**IFeatureBuildable**](), [**IRelationshipBuildable**]()
and [**IConstraintBuildable**]().

The package includes the following _built-in builders_:

1. [**FeatureBuilder**]() - can create a root feature or a not-root feature with a given _name_ and _id_
2. [**RelationshipBuilder**]() - allows to create four types of relationships: _mandatory_, _optional_, _or_ and _alternative_
3. [**ConstraintBuilder**]() - is able to create a complex cross-tree constraint

#### Parsers

The package currently provides five parsers corresponding to five following feature model formats:

1. [**SXFMParser**]() supports [SPLOT feature models](splot-research.org). The file extension could be “.sxfm” or “.splx.”
2. [**FeatureIDEParser**]() supports the feature model format of the [FeatureIDE tool](https://featureide.github.io). The file extension should be “xml.”
3. [**XMIParser**]() supports the feature model format of the v.control tool. The file extension should be “xmi.”
4. [**GlencoeParser**]() supports the feature model format of the [Glencoe tool](https://glencoe.hochschule-trier.de). The file extension should be “json.”
5. [**DescriptiveFormatParser**]() supports our feature model format. The file extension should be “fm4conf”.

You can find examples of the above feature model formats in the [**resources**]() folder.

The package also provides the [**FMParserFactory**]() class that can create a parser for a given feature model file.

#### Descriptive format

Our feature model format is a text-based format that allows us to represent a basic feature model in a human-readable way.

The feature model file should contain the following sections:

1. A header line that mentions the version of the format
2. A model name section starting with a "MODEL:" line that contains the name of the feature model
3. A features section starting with a "FEATURES:" line that contains the list of features, each feature in a new line
4. A relationships section starting with a "RELATIONSHIPS:" line that contains the list of relationships, each relationship in a new line
5. A constraints section starting with a "CONSTRAINTS:" line that contains the list of cross-tree constraints, each constraint in a new line

An example of the feature model file in the descriptive format is available in the [**resources**]() folder.