## ca-cdr-package

| **groupID**    | **_at.tugraz.ist.ase_** |
|----------------|-------------------------|
| **artifactID** | **_ca-cdr-v2_**         |
| **version**    | **_1.3.9-alpha-42_**    |

TBD

[//]: # (CA-CDR package)

[//]: # (Link https://github.com/manleviet/CA-CDR-V2/tree/main/ca-cdr-package)

[//]: # (This package provides consistency-based algorithms &#40;you don’t need to know these algorithms&#41; and &#40;most important to you&#41; a consistency checker, the ChocoConsistencyChecker class. The ChocoConsistencyChecker provides a variant of isConsistent functions with different parameters. In the current version 1.2.20, the class provides five isConsistent functions as the following:)

[//]: # (1.	isConsistent&#40;Collection<Constraint> C&#41; – checks the consistency of set of constraints)

[//]: # (2.	isConsistent&#40;Collection<Constraint> C, ITestCase testcase&#41; – checks the consistency of a test case with the background knowledge &#40;a set of constraints&#41;.)

[//]: # (3.	Set<ITestCase> isConsistent&#40;Collection<Constraint> C, Collection<ITestCase> TC, boolean onlyOne&#41; – checks the consistency of a set of test cases with the background knowledge &#40;a set of constraints&#41;. The function returns violated test cases. If onlyOne=true, the function returns the first violated test case.)

[//]: # (4.	isConsistent&#40;ITestCase testcase, ITestCase neg_testcase&#41; – checks the consistency between two test cases &#40;testcase /\ ¬neg_testcase&#41; to identify a redundant test case. If the output is false &#40;inconsistent&#41;, then neg_testcase is a redundant test case. This function is used in the WipeOutR_T algorithm.)

[//]: # (5.	isConsistent&#40;Collection<Constraint> C, Constraint cstr&#41; – checks the consistency of &#40;C - {cstr} ∪ {¬cstr}&#41; to identify the redundant constraints. If the output is false &#40;inconsistent&#41;, then cstr is a redundant constraint. This function is used by the WipeOutR_FM algorithm.)

[//]: # (      You can inherit the ChocoConsistencyChecker to add your new isConsistent function, or you can ask me to help you.)

[//]: # (      Example – how to use the two last isConsistent functions:)

[//]: # (-	two algorithms WipeOutR_FM and WipeOutR_T)
