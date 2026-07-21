# HiConfiT-Core: Project Overview & Requirements

**Version:** 1.0.1-alpha-49 | **License:** MIT | **Language:** Java 23

## What is HiConfiT-Core

HiConfiT-core is a Maven-based research library suite implementing High-Performance Knowledge Based Configuration Techniques. The project delivers production-grade implementations of:

- **Consistency-based algorithms** for Conflict Detection & Resolution (CDR) across knowledge base engineering phases (design, testing, configuration)
- **Feature model analysis** with automated anomaly detection and diagnosis
- **Knowledge-based configuration** with Matrix Factorization-based recommendation heuristics

All constraint solving is backed by a single deterministic engine: **Choco Solver 4.10.14**.

## Target Users & Motivation

**Researchers** in:
- Knowledge-based configuration systems
- Software product line (SPL) engineering
- Automated debugging and diagnosis
- Configuration analytics

**Academic grounding:** Implementations directly back peer-reviewed publications listed in [README.md](../README.md#references). Cite applicable papers when using these libraries in research.

## 11 Libraries Overview

| Artifact | Purpose | Maturity |
|---|---|---|
| **common** | CLI parsing (args4j), TOML config, CSV utilities, Choco wrappers | Foundation |
| **eval** | Global performance counters & timers; thread-scoped tracing | Foundation |
| **fm** | Feature model representation; 5-format parser (SXFM, FeatureIDE, GLENCOE, XMI, Descriptive) | Core, ~6.6kLOC |
| **csp2choco** | CSP constraint text → Choco constraint translation (ANTLR-based, flat comparisons only) | Limited scope |
| **kb** | Abstract knowledge base + FMKB (feature models), PCKB, RenaultKB, CameraKB; dual constraint representation (SAT/CSP) | Core, ~4.6kLOC |
| **ca-cdr-core** | Test cases, solutions, assignments, value objects; translators (SPI) | Foundation, ~2.1kLOC |
| **cdrmodel** | CDR problem setup; separates correct constraints from faulty ones; model factories | Core, ~2.7kLOC |
| **ca-cdr** | **9 implemented CDR algorithms** (see Algorithms); ChocoConsistencyChecker; labeler/pruning composition | Flagship, ~3.9kLOC |
| **heuristics** | Matrix Factorization-based variable/value ordering; Mahout 0.9 backend | Research-stage, no in-repo callers |
| **configurator** | Knowledge-based configurator; 10+ `findSolutions` overloads; optional MFVVO heuristics | ~450 LOC |
| **fma** | Feature model testing & debugging; 6 anomaly analyses (void FM, dead feature, false optional, etc.) with explanators; HSDAG diagnosis | Research-stage, ~3.5kLOC |

**Dependency DAG:** common ← (eval, fm, csp2choco) ← kb ← ca-cdr-core ← (cdrmodel, heuristics) ← ca-cdr ← (configurator, fma)

## Functional Requirements Delivered

### CDR Algorithms (9 implemented)
1. **QuickXPlain** — conflict detection via divide-and-conquer
2. **FastDiagV2** — consistency-based diagnosis (AC-driven)
3. **FastDiagV3** — MSS-based diagnosis (B-driven)
4. **FlexDiag** — anytime diagnosis with granular control
5. **DirectDebug** — diagnosis + test case validation
6. **DirectDiag** — MSS then compute diagnosis
7. **WipeOutR_FM** — feature model redundancy detection
8. **HSTree** — Reiter hitting set tree (generic framework)
9. **HSDAG** — hitting set DAG with pruning and node reuse

Composition layer: 6 labeler adapters + 2 pruning engines. Labelers extend flat algorithms, implement `IHSLabelable`. Engines (HSTreePruningEngine, HSDAGPruningEngine) injected post-construction.

### Feature Model Analysis (6 anomalies)
Auto-detect and explain: **Void FM, Dead Features, Conditionally Dead Features, Full Mandatory Features, False Optional Features, Redundant Constraints**.

Assumption generation (test cases) → consistency checking → HSDAG diagnosis → explanation. All anomaly paths except redundancy use DirectDebug.

**5 explanators are byte-identical (~250 LOC duplication)** — architectural simplification opportunity.

### Parsers (5 formats)
SXFM, FeatureIDE, GLENCOE, XMI, Descriptive (FM4Conf via ANTLR). Generic `FeatureModel<F,R,C>` with builder pattern; subtypes extend via alternative builders, not subclassing.

### MF-Based Configuration
Matrix Factorization heuristics (Mahout SVD++, Pearson similarity, neighborhood averaging) for variable/value ordering. Integrated into Configurator via `MFVVOVariableSelector`, `MFVVOValueSelector`.

### Dual Constraint Representation
Every `Constraint` maintains:
- High-level textual form ("f1 ⇒ f2")
- Positive Choco constraint list
- Negative Choco constraint list (for negation support in IntVar KBs)

## Non-Functional Requirements

| Requirement | Status |
|---|---|
| **JVM Target** | Java 23 (source & target in maven-compiler-plugin 3.13.0) |
| **Constraint Engine** | Choco Solver 4.10.14 exclusively; no solver abstraction |
| **Distribution** | Maven artifacts on GitHub Packages; published to `https://maven.pkg.github.com/HiConfiT/hiconfit-core` |
| **License** | MIT (21 lines) |
| **Performance Instrumentation** | Global PerformanceEvaluator (counters, timers, thread-scoped tracing via ConcurrentHashMap) |
| **Test Framework** | JUnit 5 (5.10.3), no framework mocking required |
| **Code Generation** | Lombok 1.18.38 (@Builder, @Getter, @UtilityClass, @EqualsAndHashCode, @Setter) |
| **CI/CD** | None (removed 2023-05-25; manual `mvn deploy` with GitHub credentials) |
| **Build Reproducibility** | Maven 3.x; no Wrapper, no Dockerfile, no shell scripts |

## Explicit Non-Goals & Current Limitations

**Intended scope limits:**
- **csp2choco:** Only flat integer comparisons (==, <, >, etc.); requirements, boolean connectives, arithmetic omitted from ANTLR grammar (parsed but silently discarded)
- **fma:** No timeout support (commented-out fields; TODO: "support timeout" — lines 46-71 of AbstractFMAnalysis.java)
- **IntVar negation:** KBAssignmentsTranslator.translate() has two `// TODO - negation` comments; negation unimplemented for non-FM KBs
- **heuristics MF pipeline:** No in-repo callers; persistence (`saveMF`, `loadMF`) are empty no-op methods

**Known issues (tracked in local `94-fix-fma` branch):**
- FalseOptionalAnalysis.java:49 sets `AnomalyType.VOID` instead of `FALSEOPTIONAL`
- ConditionallyDeadAnalysis.java:48 sets `AnomalyType.DEAD` instead of `CONDITIONALLYDEAD`

**Documentation accuracy issues (README vs. code):**
- README lists `WipeOutR_T` as implemented; no such class exists (only a Javadoc trace in ChocoConsistencyChecker.java:142)
- README version table is maintained by hand per artifact, so it must be re-edited on every release (currently `1.0.1-alpha-49`)
- Documentation URL: `https://hiconfit.github.io` (canonical; README updated 2026-07-21)

## Build & Test Metrics

| Category | Count |
|---|---|
| **Source files (main)** | 249 |
| **Lines of code (main, measured)** | ~27.8k |
| **Test files** | 60 |
| **Test methods (@Test)** | ~300 |
| **Test coverage** | Uneven: fm/ca-cdr/fma well-covered; kb translator/, heuristics MF pipeline, fma explanation/ untested |

## Key Architectural Patterns

1. **Knowledge Base Abstraction** — `KB` base with `defineVariables()`, `defineConstraints()`, `reset()` contract; FMKB bridges features/relationships to Choco BoolVars
2. **Translator SPI** — `IAssignmentsTranslatable`, `ISolutionTranslatable`, `ITestCaseTranslatable` decouple domain objects from Choco translation
3. **Builder Pattern** — Feature/Relationship/Constraint builders separate construction from representation; enables subtype extension without parser changes
4. **Dual Constraint Lists** — positive + negative Choco constraints per Constraint object; enables not-KB support
5. **HS Framework + Composition** — Generic HSTree with injected `IHSLabelable` labeler (6 implementations) + `IPruningEngine` (2 implementations); polarity (conflict vs. diagnosis) resolved at accessor level

## Dependencies & Risk Profile

| Dependency | Version | Risk Notes |
|---|---|---|
| Choco Solver | 4.10.14 | Core engine; sole constraint solver |
| Lombok | 1.18.38 | Code generation via annotation processor |
| Guava | 33.2.1-jre | Collections, Preconditions, @Beta markers |
| SLF4J API + Logback | 2.0.12 / 1.5.13 | Logging |
| args4j | 2.37 | CLI argument parsing |
| **mahout-core** | **0.9 (2014)** | **OLDEST dependency; version-mismatched with mahout-math 0.13.0; no known updates** |
| mahout-math | 0.13.0 | Pre-release version; excludes guava + slf4j-api |
| jackson-dataformat-toml | 2.18.1 | TOML config loading |
| ANTLR runtime | 4.13.1 | Grammar execution; 3-version skew with generated code (4.12.0) and fm4conf (4.13.2) |
| sxfm | 1.0 | SPLOT library (FM format) |
| org.json | 20240303 | JSON parsing (bumped for DoS fix in 918124b) |
| jakarta.mail | 2.0.1 | SMTP (MailService utility) |

**Build infrastructure risk:** ANTLR plugin mis-scoped into `<dependencies>` instead of `<plugins>` in 2 module poms (csp2choco, fm); grammars do not regenerate; generated code is hand-committed. Three ANTLR versions coexist.

---

**Next:** See [project-roadmap.md](./project-roadmap.md) for prioritized work backlog.
