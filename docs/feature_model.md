## fm-package

Name: fm-v2

The package provides two essential things:

1. FeatureModel object (FeatureModel class - link), which can store the information of a feature model. 
2. Parsers, which read feature models from files, translate them to FeatureModel objects. The package provides five parsers (link) for five feature model formats as the following:
      a.	SXFMParser – supports SPLOT feature models (splot-research.org). The file extension can be “.sxfm” or “.splx.” The content structure in these two files doesn’t have any difference.
      b.	FeatureIDEParser – supports the feature model format of the FeatureIDE tool. The file extension should be “xml.”
      c.	XMIParser – supports the feature model format of the v.control tool. The file extension should be “xmi.”
      d.	GlencoeParser – supports the feature model format of the Glencoe tool. The file extension should be “json.”
      e.	DescriptiveFormatParser – supports my feature model format. The file extension should be “fm4conf”.


FeatureModel is a generic class

### How to read a feature model from a file

```java
// create builders for features, relationships, and constraints using built-in builders
FeatureBuilder featureBuilder = new FeatureBuilder();
ConfRuleTranslator ruleTranslator = new ConfRuleTranslator();
RelationshipBuilder relationshipBuilder = new RelationshipBuilder(ruleTranslator);
ConstraintBuilder constraintBuilder = new ConstraintBuilder(ruleTranslator);

// create a parser for the given file
FMParserFactory<Feature, AbstractRelationship<Feature>, CTConstraint> factory = FMParserFactory.getInstance(featureBuilder, 
                                                                                    relationshipBuilder, constraintBuilder);
@Cleanup("dispose")
FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = factory.getParser("filename");

// parse the feature model file
FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> fm = parser.parse("path/to/file");
```

The simpler way:

```java
// create a parser for the given file
// getIntance returns a parser which uses the built-in builders
FMParserFactory<Feature, AbstractRelationship<Feature>, CTConstraint> factory = FMParserFactory.getInstance();
@Cleanup("dispose")
FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = factory.getParser("filename");

// parse the feature model file
FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> fm = parser.parse("path/to/file");
```

### Configuration rule translator



### Builders

The package provides there following interfaces for building features, relationships, and cross-tree constraints:

1. IFeatureBuildable
2. IRelationshipBuildable
3. IConstraintBuildable

Besides, the package provides there default builders:

1. FeatureBuilder
2. RelationshipBuilder
3. ConstraintBuilder

These builders support default types of features, relationships, and constraints.
However, if you want to use your own types of features, relationships, and constraints, 
you can implement the above interfaces and use them to build your own features, relationships, and constraints.

### Abstract Syntax Tree

To support

### Parsers

Our feature model supports the following feature model formats:

1. [SPLOT feature models](splot-research.org). The file extension could be “.sxfm” or “.splx.”
2. [FeatureIDE format](https://featureide.github.io). The file extension should be “xml.”
3. v.control format. The feature model format of the v.control tool. The file extension should be “xmi.”
4. [Glencoe format](https://glencoe.hochschule-trier.de). The file extension should be “json.”
5. Descriptive format. Our feature model format. The file extension should be “fm4conf”.

#### Feature model parser factory

The package provides a feature model parser factory which can create a feature model parser which corresponds with the format of the given feature model.

### Descriptive format

The descriptive format is a text-based format for representing basic feature models.