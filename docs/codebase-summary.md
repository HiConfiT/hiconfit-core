# HiConfiT-Core Codebase Summary

**Version:** 1.0.1-alpha-48 | **Date:** 2026-07-21 | **Java:** 23 | **Total:** 11 modules, 249 main files, ~27.8k LOC

---

## Scale Metrics

| Module | artifactId | Main Files | Main LOC | Test Files |
|--------|-----------|-----------|----------|-----------|
| fm-package | fm | 42 | 6,599 | 9 |
| kb-package | kb | 17 | 4,592 | 14 |
| ca-cdr-package | ca-cdr | 35 | 3,920 | 12 |
| fma-package | fma | 51 | 3,492 | 6 |
| cdrmodel-package | cdrmodel | 29 | 2,724 | 5 |
| ca-cdr-core-package | ca-cdr-core | 38 | 2,103 | 6 |
| csp2choco-package | csp2choco | 6 | 1,859 | 1 |
| heuristics-package | heuristics | 12 | 927 | 1 |
| common-package | common | 13 | 678 | 4 |
| configurator-package | configurator | 2 | 450 | 1 |
| eval-package | eval | 4 | 441 | 1 |
| **TOTAL** | | **249** | **~27.8k** | **60** |

---

## Per-Module Details

### 1. common (13 files / 678 LOC) — Core Utilities
**Purpose:** Static utilities, Choco integration, CLI scaffolding.
**Subpackages:** root, `cfg/`, `cli/`
**Key classes:**
- `ChocoSolverUtils` (113) — model/constraint operations, variable lookup, LogOp building
- `LoggerUtils` — thread-scoped indentation for nested traces
- `CmdLineOptionsBase`, `CmdLineOptionsWithCfg` — args4j-based CLI parsing
- `IOUtils`, `StringUtils`, `RandomUtils`, `ThreadUtils`, `ConsoleColors`
- `BaseAppConfig`, `TomlConfigLoader` — Jackson TOML configuration

**Dependency:** None (foundation)

---

### 2. eval (4 files / 441 LOC) — Performance Instrumentation
**Purpose:** Global static counters/timers for algorithm benchmarking.
**Key classes:**
- `PerformanceEvaluator` (277) — thread-scoped naming, shared/common timers, reset/getResults
- `AbstractEvaluator`, `Counter`, `Timer` — base types

**Limitation:** All static state; NOT reentrant across threads/tests.
**Dependency:** common

---

### 3. fm (42 files / 6,599 LOC) — FEATURE MODEL REPRESENTATION (LARGEST)
**Purpose:** Model parsing (5 formats), builder injection seam for type extensions.
**Subpackages:** `core/`, `core/ast/`, `builder/`, `parser/` (8 parsers + ANTLR gen), `translator/`, `factory/`
**Key classes:**
- `FeatureModel<F, R, C>` (547) — generic 3-type container, breadth/depth-first features, relationships, cross-tree constraints
- `Feature` — name, parents, children, children-cardinality
- 4 Relationship types: Mandatory, Optional, Or, Alternative (extend `AbstractRelationship<F>`)
- `CTConstraint` — cross-tree formula with optional CNF
- `ASTNode` and 8 subtypes (Composite pattern)
- 5 Parsers (SXFMParser 320, FeatureIDEParser 385, GLENCOEParser 350, XMIParser 349, DescriptiveFormatParser 276 + ANTLR)
- 3 Builder interfaces: `IFeatureBuildable` (2 methods), `IRelationshipBuildable` (4), `IConstraintBuildable` (26)
- `FMParserFactory` — factory dispatch
- `ConfRuleTranslator` — 4-way/8-way instanceof chains

**Issues:** ASTBuilder.convertToCNF untested; 26-method IConstraintBuildable DSL unchecked by tests; 3 parsers marked `@Beta`.
**Dependency:** common

---

### 4. csp2choco (6 files / 1,859 LOC) — CSP Text → Choco Model
**Purpose:** ANTLR-based CSP constraint parser.
**Grammars:** CommonLexer.g4, CSP2Choco.g4 (both inert — plugin mis-scoped; 5 generated files are hand-committed)
**Key classes:**
- `CSP2ChocoTranslator extends CSP2ChocoBaseListener` — translates flat `IDENT op INT` to `arithm(var, op, value).post()`
- Only `exitConstraint()` implemented; boolean connectives/arithmetic/parentheses all PARSED then DISCARDED

**Defects:** Grammar/implementation gap (unimplemented grammar surface); only 1 test covering 3 flat comparisons.
**Dependency:** common

---

### 5. kb (17 files / 4,592 LOC) — Knowledge Base Abstraction
**Purpose:** Domain-agnostic KB interface; FMKB feature model specialization; hardcoded integer KBs.
**Subpackages:** `common/`, `kb/core/`, `kb/core/builder/`, `kb/fm/`, `kb/pc/`, `kb/renault/`, `kb/camera/`
**Key classes:**
- `KB` (abstract) — reset(bool), defineVariables(), defineConstraints(bool) template
- `FMKB<F, R, C>` (439) — Feature ↔ BoolVar + Domain.BOOL; 4 relationship translations; optional rootConstraint
- `RenaultKB` (1,722) — largest hand-written file; 100+ hardcoded domains; 113 `.pm` rule files inline
- `PCKB` (952), `CameraKB` (433) — explicit IntVar domains
- `Constraint` (84) — dual representation (string + chocoConstraints + negChocoConstraints); equality keyed on STRING only
- `Variable`, `Domain`, `DomainType` — core value model
- `IBoolVarKB` / `IIIntVarKB` — capability interfaces for downstream algorithm branching
- `ConstraintUtils` (239) — rendering, Choco harvesting, posting, set algebra, counter constants
- Builders: `BoolVarConstraintBuilder`, `IntVarConstraintBuilder`

**Issues:** RenaultKB test 100% commented (1831 LOC pre-rename); defineDomains() private, not in contract; defineNotKB() dead code.
**Dependency:** fm, eval

---

### 6. ca-cdr-core (38 files / 2,103 LOC) — Test Cases, Solutions, Translators
**Purpose:** Domain value model (Assignment, Solution, TestCase, Requirement) + Translator SPI.
**Subpackages:** root (9 value classes), `builder/` (5), `factory/` (5), `translator/` (8 SPI), `reader/` (4), `writer/` (6), `format/`
**Key classes:**
- `Assignment` (44) — immutable variable/value pair
- `Solution` (151) — List<Assignment> + dual Choco lists; equality on assignments only
- `TestCase` (103) — textual form + assignments + dual lists + isViolated flag
- `TestSuite` (74), `Requirement` (21)
- **Translator SPI:**
  - `IAssignmentsTranslatable` → FMAssignmentsTranslator (98, BoolVar+LogOp), KBAssignmentsTranslator (132, IntVar+arithm; negation unimplemented)
  - `ISolutionTranslatable` → FMSolutionTranslator, KBSolutionTranslator
  - `ITestCaseTranslatable` → FMTestCaseTranslator (94)
- Builders: `RequirementBuilder` (58), `FMTestCaseBuilder` (75), `XMLTestCaseBuilder` (102)
- Readers: `TestSuiteReader`, `XMLTestSuiteReader`, `SolutionReader` (107; has `static` kb field bug)
- Writers: `XMLTestSuiteWriter`, `TxtSolutionWriterWithCounter` (static counter), `MultiLineTxtSolutionWriter`

**Issues:** Translator entire package untested; KBAssignmentsTranslator negation unimplemented; SolutionReader static kb field rebinds on second instantiation.
**Dependency:** kb

---

### 7. cdrmodel (29 files / 2,724 LOC) — CDR Problem Setup
**Purpose:** AbstractCDRModel pattern; FM/KB specializations; preset configurations.
**Subpackages:** root (6), `fm/` (3), `fm/factory/` (5), `kb/` (2), `kb/factory/` (3), `test_model/` (2)
**Key classes:**
- `AbstractCDRModel` (140) — correctConstraints (B), possiblyFaultyConstraints (C); initialize() unposts Choco, leaving variables only
- `FMCdrModel<F, R, C>` (178) — 4 flags (hasNegativeConstraints, rootConstraints, cfInConflicts, reversedConstraintsOrder) controlling B/C split
- `FMRequirementCdrModel` (114), `FMDebuggingModel` (130), `KBCdrModel` (108), `KBRequirementCdrModel` (98)
- Factories: `FMCdrModelFactory`, `FMRedundancyDetectionModelFactory`, `FMDebuggingModelFactory`, etc.
- Façades: `FMCdrModels`, `KBCdrModels` — `@UtilityClass` one-call methods

**Note:** No cross-product logic (CLAUDE.md incorrect claim).
**Dependency:** ca-cdr-core, csp2choco

---

### 8. ca-cdr (35 files / 3,920 LOC) — THE ALGORITHM LIBRARY
**Purpose:** 9 algorithms (QuickXPlain, FastDiag*, FlexDiag, DirectDebug, WipeOutR_FM, HSDAG) + 6 labelers + 2 pruning engines.
**Subpackages:** `algorithms/` (9 flat), `algorithms/hs/` (7 HS-tree core), `algorithms/hs/labeler/` (8), `algorithms/hs/parameters/` (7), `checker/` (2), `eval/`
**Key classes:**
- Flat algorithms: QuickXPlain, FastDiagV2/V3, FlexDiag, DirectDiag, DirectDebug, WipeOutR_FM (+ deprecated FastDiag)
- HS machinery: `AbstractHSConstructor`, `HSTree` (271), `HSDAG` (84, extends HSTree), `Node` (194)
- Pruning: `HSTreePruningEngine` (124), `HSDAGPruningEngine` (157) — separate injection, null-default hazard
- Labelers (extend flat algo + implement IHSLabelable): QuickXPlainLabeler, FastDiagV2/V3/DirectDiagLabeler, FlexDiagLabeler, DirectDebugLabeler
- Parameters (per labeler): AbstractHSParameters, QuickXPlainParameters, etc.
- `ChocoConsistencyChecker` (373) — 5+ isConsistent overloads; exception catch-all; System.gc() per HS-node

**Hazards:** null-default pruningEngine (NPE), HSDAG downcasts pruningEngine (CCE), System.gc() per node, exceptions swallowed.
**Dependency:** cdrmodel

---

### 9. heuristics (12 files / 927 LOC) — Matrix Factorization VVO
**Purpose:** Matrix Factorization-based variable/value ordering (MF half DEAD from codebase).
**Subpackages:** root (4), `mf/` (2), `io/` (4), `selector/` (2)
**Key classes:**
- `MFVVOHeuristic` (270), `MFVVOModel` (139) — MF pipeline building + recommendation
- `MatrixFactorization` (109) — wraps Mahout SVDPlusPlus factorizer
- `ValueVariableOrdering`, `ValueOrdering` — heuristic output
- `MFVVOVariableSelector`, `MFVVOValueSelector` — Choco integration (O(n·m) list surgery per branch)
- Readers: `ValueVariableOrderingReader`, `ValueOrderingReader`

**Dead finding:** MFVVOHeuristic, MFVVOModel, MatrixFactorization have **no in-repo callers**; saveMF/loadMF empty no-ops. Heuristics half (io/ + selector/) consumed; MF half is dead.
**Dependency:** ca-cdr-core

---

### 10. configurator (2 files / 450 LOC) — Knowledge-Based Configuration
**Purpose:** KB-driven configuration with optional MFVVO heuristics.
**Key classes:**
- `ConfigurationModel` (98) — extends AbstractCDRModel; B = all KB constraints
- `Configurator` (352, 2nd largest) — 10 findSolutions overloads; control vs. compact modes; VVO support

**Smells:** find() deduplicates via linear scan O(n); find() returns inverted-sounding could_more_solution; clearVVO() restores search but the setSearch call is commented out.
**Dependency:** ca-cdr, heuristics

---

### 11. fma (51 files / 3,492 LOC) — Feature Model Anomaly Analysis
**Purpose:** 6 anomaly detections + explanations via 4-way symmetry pattern.
**Subpackages:** root (FMAnalyzer 160), `analysis/` (8), `anomaly/` (4), `assumption/` (7), `builder/` (8), `explanator/` (8), `explanation/` (5), `monitor/`, test subdirs (7)
**Key classes:**
- `AnomalyType` enum (6 constants) — wires builder per anomaly
- `AbstractFMAnalysis extends RecursiveTask` — analyze() dispatch; no fork/join recursion
- 6 Analyses: VoidFMAnalysis, DeadFeatureAnalysis, ConditionallyDeadAnalysis, FullMandatoryAnalysis, FalseOptionalAnalysis, RedundancyAnalysis
- 5 Explanators (byte-identical ~50 LOC) — all use DirectDebugLabeler → HSDAG
- 6 Assumption generators, 6 Builders
- `AnomalyAwareFeature` (83) — extends Feature; anomalies list; reuse <F,R,C> via generics without subclassing FeatureModel

**Concurrency:** `ForkJoinPool.commonPool()` submit + join (no shutdown effect); staged execution (VOID → DEAD → rest); parallelStream() throughout.
**Issues:** 2 silent bugs (FalseOptionalAnalysis sets VOID not FALSEOPTIONAL; ConditionallyDeadAnalysis sets DEAD not CONDITIONALLYDEAD); 5 byte-identical explanators; no timeout support.
**Dependency:** ca-cdr

---

## Where to Look For...

| Task | Directory/Files |
|------|-----------------|
| Add a new FM parser | `fm/parser/` — implement `FeatureModelParser`, register in `FMParserFactory.java` |
| Add a CDR algorithm | `ca-cdr/algorithms/` — extend `IConsistencyAlgorithm`, optionally add labeler in `ca-cdr/algorithms/hs/labeler/` |
| Change constraint translation | `kb/FMKB.java` (relationship types), `csp2choco/` (CSP syntax), `ca-cdr-core/translator/` (domain→Choco) |
| Add an anomaly analysis | `fma/assumption/`, `fma/analysis/`, `fma/explanator/`, `fma/builder/` + entry in `AnomalyType` enum |
| Performance work | `eval/PerformanceEvaluator.java` (counters/timers), `ca-cdr/checker/ChocoConsistencyChecker.java` (solver instrumentation) |
| Test suite builders | `ca-cdr-core/builder/fm/`, `cdrmodel/` test resources, `fma/test/` |
| Value model & equality | `ca-cdr-core/` (Assignment, Solution, TestCase, Constraint) — **note: Constraint.equals() keys on STRING only** |
| HS-Tree composition | `ca-cdr/algorithms/hs/` (HSTree, HSDAG, labelers, pruning engines) |

---

## Package Naming Convention

All packages follow: **`at.tugraz.ist.ase.hiconfit.<module>.<subpackage>`**

Common subpackages:
| Subpackage | Usage | Examples |
|------------|-------|----------|
| `core` | Core domain/abstractions | kb.core, fm.core, ca-cdr-core |
| `builder` | Builder patterns | fm.builder, fma.builder |
| `parser` | File format parsers | fm.parser |
| `translator` | Domain→solver bridges | ca-cdr-core.translator, kb.translator |
| `algorithm` | Algorithm implementations | ca-cdr.algorithms |
| `checker` | Consistency/validation | ca-cdr.checker |
| `io` | Input/output | heuristics.io |
| `factory` | Factory patterns | fm.factory, cdrmodel.factory |
| `analysis` | Analysis logic | fma.analysis |
| `explanator` | Explanation generation | fma.explanator |
| `assumption` | Test case generation | fma.assumption |

---

## Files Exceeding ~300 LOC Java Threshold

| File | LOC | Issue |
|------|-----|-------|
| RenaultKB.java | 1,722 | Largest main file; 100+ hardcoded domains; test file 100% commented |
| PCKB.java | 952 | Hardcoded CSP KB |
| FeatureModel.java | 547 | Only hand-written main source meaningfully over threshold |
| AutomatedAnalysisBuilder.java | 520 | 429 lines (82%) are commented-out dead code |
| FMKB.java | 439 | Generic FM KB |
| CameraKB.java | 433 | Hardcoded camera KB |
| FeatureIDEParser.java | 385 | FeatureIDE XML → Feature model |
| ChocoConsistencyChecker.java | 373 | 5 isConsistent overloads; largest main file in ca-cdr |
| Configurator.java | 352 | 10 findSolutions overloads |
| GLENCOEParser.java | 350 | GLENCOE JSON → Feature model |
| XMIParser.java | 349 | XMI → Feature model |
| SXFMParser.java | 320 | SXFM/SPLOT → Feature model |

**Test files over 300 LOC:** PCKBTest 1,646, SolveErrorTest 1,620 (all commented), HSDAGTest 1,206, HSTreeTest 1,002, FMAnalyzerTest 2,219, + 6 others.

---

## Test Coverage Gaps (Significant)

| Module/Area | Files | Notes |
|-------------|-------|-------|
| ca-cdr-core/translator/ | 8 files | **Entire package untested** — most logic-dense part |
| heuristics MF pipeline | 3 files | MFVVOHeuristic, MFVVOModel, MatrixFactorization + selectors; no callers, no tests |
| fm/builder/ | 6 files | All untested (IConstraintBuildable's 26 methods, FeatureModels, ConfRuleTranslator) |
| fm/ASTBuilder.convertToCNF | 1 method | Module's most intricate logic; only CNF test is commented out |
| kb/RenaultKB | 1 file | 1,722 LOC, zero live assertions; test file pre-rename, won't compile |
| kb/SolveErrorTest | 1 file | 1,620 LOC, 100% commented out |
| ChocoConsistencyChecker | 373 LOC | No dedicated unit test; clone-model paths untested; isConsistent(tc, neg_tc) WipeOutR_T hook untested |
| fma/explanation/ + explanator/ | 10 files | No direct unit tests; 5 byte-identical explanators never exercised independently |
| fma/anomaly bugs | — | No test asserts isAnomalyType(FALSEOPTIONAL) or isAnomalyType(CONDITIONALLYDEAD) — bugs survive |
| common utilities | 13 files | ChocoSolverUtils, LoggerUtils (concurrency-sensitive), MailService, cfg/*, ThreadUtils, cli/* — untested |
| cdrmodel factories | 8 files | All 8 factories + 2 façades + both KB CDR models — untested |

**3 test files are ~100% commented (3,070 LOC dead):** RenaultKBTest, SolveErrorTest, ASTBuilder.convertToCNF test. Together they represent 37% of kb's test LOC.

---

## Key Observations

**Strengths:**
- Clean layered architecture with clear separation of concerns
- Powerful builder injection seam (fm) enabling type-safe extensions without parser rewrites
- Sophisticated hitting-set composition machinery (IHSLabelable seam)
- Generic `<F, R, C>` triple supports multiple feature model variants

**Weaknesses:**
- **Global static state:** PerformanceEvaluator, FMAssignmentsTranslator, SolutionReader, SolutionWriterWithCounter — non-reentrant, breaks testing
- **Significant test gaps:** translator SPI, builder machinery, anomaly analysis, heuristics, common utilities untested
- **Dead code:** heuristics MF half, FastDiag (deprecated), commented-out logic in 8+ files
- **ANTLR plugin mis-scoped:** grammars inert; generated code hand-committed (3-way version skew)
- **Documentation gaps:** README lists missing WipeOutR_T; CLAUDE.md has wrong dependency graph
- **Known bugs:** fma anomaly tags set incorrectly; System.gc() per HS-node
