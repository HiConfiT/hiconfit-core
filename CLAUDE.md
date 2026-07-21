# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

HiConfiT-core is a set of Maven-based libraries for High-Performance Knowledge Based Configuration Techniques. The codebase implements consistency-based algorithms for Conflict Detection and Resolution (CDR), Feature Model Analysis, and knowledge-based configuration using Choco Solver as the constraint solving engine.

**Documentation:** https://hiconfit.github.io

## Build Commands

This is a Java 23 Maven multi-module project using Lombok for code generation.

### Build entire project
```bash
mvn clean install
```

### Build specific module
```bash
mvn clean install -pl <module-name> -am
```
Example: `mvn clean install -pl ca-cdr-package -am`

### Run all tests
```bash
mvn test
```

### Run tests in specific module
```bash
mvn test -pl <module-name>
```

### Run a single test class
```bash
mvn test -pl <module-name> -Dtest=<TestClassName>
```
Example: `mvn test -pl ca-cdr-package -Dtest=FastDiagV2Test`

### Run a single test method
```bash
mvn test -pl <module-name> -Dtest=<TestClassName>#<methodName>
```
Example: `mvn test -pl ca-cdr-package -Dtest=FastDiagV2Test#testDiagnosis`

### Skip tests during build
```bash
mvn clean install -DskipTests
```

## Module Architecture

The project consists of 11 Maven modules organized in a layered dependency structure:

### Layer 1: Foundation
- **common** (no intra-project deps): Core utilities, Choco Solver integration, CLI tools (args4j), CSV/email utilities

### Layer 2: Representation (all depend only on `common`)
- **eval**: Performance evaluation framework with counters and timers
- **fm**: Feature model representation with features, relationships, and cross-tree constraints. Supports multiple parsers (SXFM, FeatureIDE, GLENCOE, XMI)
- **csp2choco**: CSP constraint expression parser (ANTLR-based) that translates to Choco Model. Note: this does *not* feed `kb` — it enters at the `cdrmodel` layer

### Layer 3: Knowledge Base
- **kb** (deps: `fm`, `eval`): Abstract knowledge base model with variables, domains, and constraints. Includes FMKB (feature model specific KB)

### Layer 4: Core CDR
- **ca-cdr-core** (deps: `kb`): Test cases, solutions, assignments, and translator interfaces for CDR
- **cdrmodel** (deps: `ca-cdr-core`, `csp2choco`): CDR model abstractions and FM-specific implementations (FMCdrModel, FMRequirementCdrModel)
- **heuristics** (deps: `ca-cdr-core`): Variable/value ordering heuristics using Matrix Factorization (MFVVO). Branches off `ca-cdr-core` directly, bypassing `cdrmodel`/`ca-cdr`

### Layer 5: Algorithms
- **ca-cdr** (deps: `cdrmodel`): Implementations of CDR algorithms (QuickXPlain, FastDiag, FlexDiag, DirectDebug, HSDAG, WipeOutR_FM, etc.)

### Layer 6: Applications & Extended Analysis
- **configurator** (deps: `ca-cdr`, `heuristics`): Knowledge-based configurator supporting Matrix Factorization-based configuration
- **fma** (deps: `ca-cdr`): Feature model anomaly detection and debugging (dead features, false optionals, redundant constraints)

## Key Architectural Patterns

### 1. Knowledge Base Abstraction
All constraint problems are represented through the abstract `KB` class, which provides:
- `defineVariables()`: Creates solver variables (BoolVar, IntVar)
- `defineConstraints()`: Converts domain constraints to Choco constraints
- `reset()`: Reinitializes the model

`FMKB<F, R, C>` extends KB for feature models, bridging Features/Relationships/Constraints to Choco's BoolVar and constraints.

### 2. Dual Constraint Representation
Each `Constraint` object maintains both:
- High-level string representation (e.g., "f1 => f2")
- Low-level Choco constraint list (`List<ChocoConstraint>`)
- Separate positive and negative constraint lists for negation support

### 3. CDR Model Pattern
`AbstractCDRModel` separates:
- `correctConstraints`: Background knowledge (always true)
- `possiblyFaultyConstraints`: Knowledge base to diagnose

`FMCdrModel` extends this for feature models, configured by four constructor flags
(`hasNegativeConstraints`, `rootConstraints`, `cfInConflicts`, `reversedConstraintsOrder`)
with two canonical presets: diagnosis/conflict detection, and WipeOutR_FM redundancy
detection. `FMDebuggingModel` adds a test suite and translates each test case 1:1 into
the KB — there is no cross-product between constraints and test cases.

### 4. Translator Pattern
Translators bridge domain representations to solver constraints:
- `ISolutionTranslatable`: Converts `Solution` → `List<Constraint>`
- `ITestCaseTranslatable`: Converts `TestCase` → Choco constraints
- Format parsers (SXFM, FeatureIDE, XML) isolated from core algorithms

### 5. Generic Type Parameters
`FeatureModel<F, R, C>` and `FMKB<F, R, C>` use generics to support different feature model variants (e.g., `AnomalyAwareFeature` in fma-package) without code duplication.

## Important Classes and Interfaces

### Core Abstractions
- `KB`: Abstract knowledge base with Choco Model
- `FMKB<F, R, C>`: Feature model KB implementing `IBoolVarKB`
- `AbstractCDRModel`: Base for CDR problem setup
- `FMCdrModel<F, R, C>`: Feature model CDR configuration

### Domain Objects
- `Variable`: Abstract variable (BoolVariable, IntVariable)
- `Domain`: Value sets with Choco integer mappings
- `Constraint`: High-level constraint with Choco representations
- `Assignment`: Variable-value pair
- `TestCase`: List of assignments representing a test
- `Solution`: List of assignments satisfying requirements

### Algorithms
- `IConsistencyChecker`: Interface for consistency checking (impl: `ChocoConsistencyChecker`)
- `IConsistencyAlgorithm`: Base for CDR algorithms
- Concrete algorithms: `FastDiag`, `DirectDebug`, `HSDAG`, `WipeOutR_FM`, etc.

### Utilities
- `ConstraintUtils` (kb-package): Constraint set formatting, Choco operations, performance tracking
- `ChocoSolverUtils` (common): Wrapper for Choco model operations
- `PerformanceEvaluator` (eval): Global counters and timers for benchmarking

## Working with Feature Models

### Loading a Feature Model
```java
// From SXFM file
FeatureModel<Feature, AbstractRelationship, CTConstraint> fm =
    new SXFMParser("path/to/model.sxfm").parse();

// From FeatureIDE file
FeatureModel<Feature, AbstractRelationship, CTConstraint> fm =
    new FeatureIDEParser("path/to/model.xml").parse();
```

### Creating a Knowledge Base
```java
FMKB<Feature, AbstractRelationship, CTConstraint> kb = new FMKB<>(fm, false);
```

### Running CDR Algorithms
```java
// Create CDR model
FMCdrModel<Feature, AbstractRelationship, CTConstraint> cdrModel =
    new FMCdrModel<>(kb, possiblyFaultyConstraints, correctConstraints);

// Run FastDiag
ChocoConsistencyChecker checker = new ChocoConsistencyChecker(cdrModel);
FastDiagV2 fastDiag = new FastDiagV2(checker);
Set<Constraint> diagnosis = fastDiag.findDiagnosis(testcase);
```

## Testing Conventions

- Tests use JUnit 5 (`@Test`, `@BeforeEach`, etc.)
- Test files located in `src/test/java` mirroring package structure
- Test resources (feature models, test suites) in `src/test/resources`
- Algorithm tests typically use small feature models from test resources
- Performance tests may use `PerformanceEvaluator` for metrics

## Common Patterns in Code

### Lombok Annotations
The codebase extensively uses Lombok:
- `@Getter`/`@Setter`: Generate getters/setters
- `@Builder`: Builder pattern for construction
- `@NonNull`: Null checking
- `@EqualsAndHashCode`: Generate equals/hashCode
- `@ToString`: Generate toString

### Choco Solver Integration
All constraint solving ultimately uses Choco:
```java
Model model = kb.getModelKB();
BoolVar[] vars = ((IBoolVarKB) kb).getBoolVars();
model.getSolver().solve(); // Returns true if consistent
```

### Performance Tracking
Use `PerformanceEvaluator` for instrumentation:
```java
PerformanceEvaluator.incrementCounter("<counter_name>");
PerformanceEvaluator.reset(); // Clear all counters
```

### Translation Pipeline
Domain → Feature Model → FMKB → Choco Model → Solutions → Output:
1. Parse with format-specific parser (SXFM, FeatureIDE, etc.)
2. Build FeatureModel
3. Create FMKB (auto-translates to Choco)
4. Wrap in FMCdrModel for algorithms
5. Run algorithms with ChocoConsistencyChecker
6. Extract solutions as Assignment lists

## Module Dependencies

When adding features or fixing bugs, be aware of the dependency flow:

This is a DAG, not a linear cascade. `common` is the only module with no
intra-project dependencies; `csp2choco` and `heuristics` sit on side branches.

```
common ──┬──► csp2choco ─────────────────────────┐
         ├──► fm ──┐                             │
         └──► eval ┴──► kb ──► ca-cdr-core ──┬───┴──► cdrmodel ──► ca-cdr ──┬──► configurator
                                             │                              │         ▲
                                             └──► heuristics ───────────────┼─────────┘
                                                                            └──► fma
```

Declared first-party dependencies, verified against each module's `pom.xml`:

| Module | Depends on |
|---|---|
| common | — |
| eval | common |
| csp2choco | common |
| fm | common |
| kb | fm, eval |
| ca-cdr-core | kb |
| cdrmodel | ca-cdr-core, csp2choco |
| ca-cdr | cdrmodel |
| heuristics | ca-cdr-core |
| configurator | ca-cdr, heuristics |
| fma | ca-cdr |

Changes to a module affect every module downstream of it in this graph.
Longest chain (6 levels): `fma → ca-cdr → cdrmodel → ca-cdr-core → kb → fm → common`.

See `docs/system-architecture.md` for the full architectural reference.

## Package Naming Convention

All packages follow: `at.tugraz.ist.ase.hiconfit.<module-name>.<subpackage>`

Common subpackages:
- `core`: Core domain classes
- `builder`: Builder pattern implementations
- `parser`: File format parsers
- `translator`: Domain-to-solver translators
- `algorithms`: Algorithm implementations
- `checker`: Consistency checkers
- `io`: Input/output utilities

## Important Files to Check

When working on specific features:

- **Adding new parsers**: See `fm-package/src/main/java/at/tugraz/ist/ase/hiconfit/fm/parser/`
- **Adding CDR algorithms**: See `ca-cdr-package/src/main/java/at/tugraz/ist/ase/hiconfit/cacdr/algorithms/`
- **Modifying constraint translation**: Check `csp2choco-package` and `kb-package/FMKB.java`
- **Performance issues**: Review `PerformanceEvaluator` usage and Choco solver configuration
- **Test case management**: See `ca-cdr-core-package/builder/fm/` for test suite builders

## Version and Dependencies

- **Java Version**: 23
- **Choco Solver**: 4.10.14
- **Lombok**: 1.18.38
- **JUnit**: 5.10.3
- **Maven Compiler**: 3.13.0
- **Maven Surefire**: 2.22.0

## Publishing to GitHub Packages

The project publishes to GitHub Packages. Authentication requires GitHub credentials in Maven `settings.xml` as documented in README.md.

Current version: `1.0.1-alpha-49` (see root pom.xml `<artifact.version>`)
