## cdrmodel-package

| **groupID**    | **_at.tugraz.ist.ase_** |
|----------------|-------------------------|
| **artifactID** | **_cdrmodel-v2_**       |
| **version**    | **_1.3.9-alpha-42_**    |

### FMCdrModel and FMDebuggingModel classes

The [**FMCdrModel**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/cdrmodel-package/src/main/java/at/tugraz/ist/ase/cdrmodel/fm/FMCdrModel.java)
and [**FMDebuggingModel**](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/cdrmodel-package/src/main/java/at/tugraz/ist/ase/cdrmodel/fm/FMDebuggingModel.java)
classes are representations of knowledge-based models for diagnosis tasks, debugging tasks as well as
analysis operations of feature models.
The main purpose of these classes is to manage two sets of constraints, i.e., a background knowledge (**B**) 
and a set of constraints to be diagnosed (**C**).
These two sets of constraints are used as inputs for the diagnosis algorithms.

These classes are generic now.
The following codes shows how to use them:

```java
File fileFM = new File("src/test/resources/survey.fm4conf");

// create a parser for the given file
@Cleanup("dispose")
FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = FMParserFactory.getInstance().getParser(fileFM.getName());
// parse the feature model file
FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> fm = parser.parse(fileFM);

// create a FMCdrModel object
FMCdrModel<Feature, AbstractRelationship<Feature>, CTConstraint> model = new FMCdrModel<>(fm, true, false, true);
model.initialize(); // assigns the constraints to the background knowledge and the constraints to be diagnosed

// create a checker
ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testCaseModel);

// get the CF
List<Constraint> CF = new LinkedList<>(testCaseModel.getPossiblyFaultyConstraints());

// execute the WipeOutR_FM to identify the redundancy-free feature model
WipeOutR_FM wipeOut = new WipeOutR_FM(checker);
reset(); // reset timers and counters
List<Constraint> newCF = wipeOut.run(CF);

System.out.println("Redundancy-free feature model:");
newCF.forEach(System.out::println);
```

```java
File fileFM = new File("src/test/resources/survey.fm4conf");

// create a parser for the given file
@Cleanup("dispose")
FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = FMParserFactory.getInstance().getParser(fileFM.getName());
// parse the feature model file
FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> fm = parser.parse(fileFM);

// read the test suite using a TestSuiteBuilder
TestSuiteReader builder = new TestSuiteReader();
FMTestCaseBuilder testCaseFactory = new FMTestCaseBuilder();
@Cleanup InputStream is = getInputStream(FMDebuggingModelTest.class.getClassLoader(), "survey.testcases");

TestSuite testSuite = builder.read(is, testCaseFactory);

FMTestCaseTranslator translator = new FMTestCaseTranslator();
// create a debugging model object
FMDebuggingModel<Feature, AbstractRelationship<Feature>, CTConstraint> model = new FMDebuggingModel<>(fm, testSuite, translator, false, true, false);
model.initialize(); // assigns the constraints to the background knowledge and the constraints to be diagnosed

// create a checker        
ChocoConsistencyChecker checker = new ChocoConsistencyChecker(model);

// execute the DirectDebug to identify the faulty constraints
DirectDebug directDebug = new DirectDebug(checker);
reset(); // reset timers and counters
Map.Entry<Set<ITestCase>, Set<Constraint>> result = directDebug.findDiagnosis(model.getPossiblyFaultyConstraints(),
                                                                              model.getCorrectConstraints(),
                                                                              model.getTestcases());

// print the result
Set<Constraint> diag = result.getValue();
System.out.println("\t\tDiag: " + diag);
```

#### Other examples

- [Unit tests of FMDebuggingModel](https://github.com/manleviet/CA-CDR-V2/tree/21-uses-generics-for-feature-model/cdrmodel-package/src/test/java/at/tugraz/ist/ase/cdrmodel/fm)
- [WipeOutRFMTest](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/ca-cdr-package/src/test/java/at/tugraz/ist/ase/cacdr/algorithms/WipeOutRFMTest.java)
- [DirectDebugTest](https://github.com/manleviet/CA-CDR-V2/blob/21-uses-generics-for-feature-model/ca-cdr-package/src/test/java/at/tugraz/ist/ase/cacdr/algorithms/DirectDebugTest.java)

[//]: # (Test cases and test suite)

[//]: # (In the context of the WipeOutR project, each test case is a conjunction of assignments, e.g., A=a1 & B=b3 & C=c2 &..., where ‘A=a1’ is an assignment, ‘&’ represents an AND operator.)

[//]: # (To feature models, test cases are simpler, e.g., A & ~B & C, where ‘A’ represents ‘A=true’ and ‘~B’ represents ‘B=false.’)

[//]: # (A test suite is a set of test cases.)

[//]: # (test-package & eval-package)

[//]: # (Link: https://github.com/manleviet/CA-CDR-V2/tree/main/test-package)

[//]: # (https://github.com/manleviet/CA-CDR-V2/tree/main/eval-package)

[//]: # (These packages provide two functionalities:)

[//]: # (1.	Test case and test suite management – provides the following classes:)

[//]: # (      a.	Assignment – manages an assignment, e.g., F1 = true or Modell = limousine.)

[//]: # (      b.	ITestCase – an interface that defines which functionalities every type of test case needs to support.)

[//]: # (      In another project, I have a new type of test case called AggregatedTestCase.)

[//]: # (      c.	TestCase – a test case that manages a list of assignments, a corresponding list of Choco constraints, and maybe a corresponding list of negative Choco constraints.)

[//]: # (      d.	TestSuite – manages a list of ITestCase.)

[//]: # (      e.	TestSuiteBuilder – provides a function &#40;buildTestSuite&#41; that reads a test suite from a test suite file. A test suite file will contain a total number of test cases in the first line and each test case written in other lines.)

[//]: # (      If you need to support other structures of test suite files, you need to implement the interface ITestSuiteBuildable.)

[//]: # (      f.	FMTestCaseBuilder – provides the capability of translating a feature model test case into a TestCase object.)

[//]: # (      To support other test case types &#40;such as Renault test case - Var1 = S64 & Var3 = M9&#41;, you need to implement the interface ITestCaseBuildable.)

[//]: # (2.	PerformanceEvaluator – an evaluator which measures algorithm runtimes or counts something.)

[//]: # (      Example – how to use TestCase and TestSuite classes:)

[//]: # (-	Unit tests)

[//]: # (-	WipeOutR_T_Model1)

[//]: # (-	WipeOutR_T_Model2)

[//]: # (-	RuntimeForTestcasesV2)

[//]: # (-	WipeOutTEvaluationV2)

[//]: # (     Example – how to use PerformanceEvaluator as well as Counter and Timer:)

[//]: # (-	Unit test)

[//]: # (-	RuntimeForTestcasesV2)

[//]: # (-	WipeOutTEvaluationV2)

[//]: # ()
[//]: # (CDR Models)

[//]: # (The input of consistency-based algorithms, such as WipeOutR_T or WipeOutR_FM, is a set of constraints or test cases. Therefore, CDRModels are created to manage/prepare the constraints/test cases before passing them to the algorithms.)

[//]: # (CDRModel package)

[//]: # (Link https://github.com/manleviet/CA-CDR-V2/tree/main/cdrmodel-package)

[//]: # (The abstract class CDRModel manages:)

[//]: # (-	a set of constraints which we assume to be always correct &#40;a background knowledge&#41; – correctConstraints.)

[//]: # (-	a set of constraints that could be faulty – possiblyFaultyConstraints.)

[//]: # (     We denote correctConstraints as B and possiblyFaultyConstraints as C in some consistency-based algorithms.)

[//]: # (     New CDRModel inheritance needs to override the initialize function, where we add constraints from the KB class to sets of constraints, i.e., add correct constraints to correctConstraints and possible faulty constraints to possiblyFaultyConstraints.)

[//]: # (     You can directly pass constraints from the KB class to consistency-based algorithms. However, in this case, it’s challenging to manage your code. With CDRModels, you know where you need to take care of constraints that will be passed to algorithms.)

[//]: # (     For the WipeOutR project, a new CDRModel class needs to implement the interface IDebuggingModel, which requires the new class to provide a set of test cases. In this context, the initialize function needs an additional step to translate test cases into Choco constraints.)

[//]: # (     The TestCaseBuilders can only read and create TestCase objects. It couldn’t translate these test cases into Choco constraints since it doesn’t know the Choco Model, in which these test cases will be checked. On the contrary, CDR models hold a Choco Model taken from a KB object. So, the responsibility of translating test cases into Choco constraints belongs to CDR models and should be implemented inside the initialize function.)

[//]: # (     Example – how to create a new CDRModel that implements the interface IDebuggingModel:)

[//]: # (-	WipeOutR_T_Model1)

[//]: # (-	WipeOutR_T_Model2)

[//]: # (-	WipeOutRTModel.java)

[//]: # (-	WipeOutRFMModel.java)

[//]: # (     Example – how to use the above models:)

[//]: # (-	RuntimeForTestcasesV2)

[//]: # (-	WipeOutTEvaluationV2)

[//]: # (-	SolverRuntimeEvaluation)

[//]: # (-	WipeOutFMEvaluationV2)
