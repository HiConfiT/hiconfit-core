# Code Standards — HiConfiT-Core

Conventions and patterns as actually practiced in this codebase. Not generic Java advice — these reflect what you'll see in the 11 modules and what works here.

**Java 23, Maven-based, Lombok 1.18.38, Choco Solver 4.10.14**

---

## Package Naming

All packages follow the absolute pattern:

```
at.tugraz.ist.ase.hiconfit.<module-name>.<subpackage>
```

Module names in code use hyphens (e.g., `ca-cdr-package`) but in package paths use underscores or drop the suffix entirely. Examples:

| Maven Module | Package Prefix |
|---|---|
| `ca-cdr-package` | `at.tugraz.ist.ase.hiconfit.cacdr` |
| `ca-cdr-core-package` | `at.tugraz.ist.ase.hiconfit.cacdr_core` |
| `cdrmodel-package` | `at.tugraz.ist.ase.hiconfit.cdrmodel` |
| `fma-package` | `at.tugraz.ist.ase.hiconfit.fma` |

Recurring subpackage vocabulary (pick semantically):
- `core` — Domain entities, primary abstractions
- `builder` — Builder pattern implementations
- `parser` — File format parsers
- `translator` — Domain-to-solver translators
- `factory` — Façade factories
- `algorithms` — Algorithm implementations
- `checker` — Consistency checkers
- `io` — I/O utilities
- `analysis` — Analysis implementations
- `anomaly` — Anomaly types
- `assumption` — Assumption generators
- `explanator` — Explanation generators
- `explanation` — Explanation models

---

## File Naming

**Java files: PascalCase, matching the public class name exactly.**

Compiler-enforced on one file per public class. Examples: `FeatureModel.java`, `FastDiagV2.java`, `ChocoConsistencyChecker.java`.

Test files: `*Test.java` convention. Example: `FastDiagV2Test.java`.

**Note:** Module directories are `<name>-package` (e.g., `ca-cdr-package/`). This mismatch between directory names and package paths is unavoidable due to Maven conventions but has confused many developers — document it clearly in onboarding.

---

## Lombok Patterns

Lombok 1.18.38 is wired via `annotationProcessorPath` in the root pom. Pervasive usage:

| Annotation | Usage |
|---|---|
| `@Getter` / `@Setter` | Standard field access generation |
| `@Builder` | Constructor-based builder; also `@Builder(builderMethodName="...")` for renamed builders (e.g., `requirementBuilder()` on Requirement) |
| `@NonNull` | Parameter null-checking and `@NonNullByDefault` on packages |
| `@EqualsAndHashCode` | With `.Include` override (e.g., Constraint uses string equality only, not Choco payload) |
| `@ToString` | Standard, avoid calling inside library methods |
| `@With` | Immutable object field copiers (Solution, Assignment) |
| `@Cleanup("dispose")` | Resource closing via custom method name (FeatureModels.fromFile pattern) |
| `@UtilityClass` | Static-only helper classes (ChocoSolverUtils, ConstraintUtils, FeatureModels, etc.) |
| `@Slf4j` | Logger generation (SLF4J binding) |
| `val` | Local-var type inference |

**Critical gotcha:** `@EqualsAndHashCode` on `Constraint` keys off the TEXT ONLY (`@EqualsAndHashCode.Include protected final String constraint`), not Choco payloads. Two constraints with identical text but different Choco lists are equal — relied on by CDR set-algebra operations and LinkedHashSet deduplication.

---

## Guava Conventions

Guava 33.2.1-jre (declared in common). Pervasive arg validation:

```java
Preconditions.checkArgument(size > 0, "Size must be positive");
Preconditions.checkNotNull(feature);
```

Set operations: `Sets.union()` returns VIEWS (not copies), `Sets.difference()`, `Sets.intersection()`.

Three newer parsers marked `@Beta`: GLENCOEParser, XMIParser, DescriptiveFormatParser. Do not depend on their stability.

---

## Resource Lifecycle — The Dispose Protocol

**NOT AutoCloseable / try-with-resources.** This codebase uses a hand-rolled disposal pattern:

```java
public void dispose()  // explicit cleanup method
```

Called on:
- `FeatureModel<F,R,C>` — closes feature/relationship deep state
- `KB` and all subclasses (FMKB, PCKB, etc.) — nulls the Choco Model reference
- `CDRModel` variants — cascades to contained KB
- All algorithms — nulls internal state

**Convention:** If your class holds a KB, algorithm, or feature model, add `dispose()` and call it explicitly after use. There is no finalizer protection.

Example:
```java
FeatureModel<Feature, AbstractRelationship, CTConstraint> fm = 
    FeatureModels.fromFile(path);
// ... use fm ...
fm.dispose();
```

The `@Cleanup("dispose")` annotation on local vars in factories handles this automatically.

---

## Dual Constraint Representation

Core pattern across CDR algorithms. Each `Constraint` object holds:

```java
@EqualsAndHashCode.Include protected final String constraint;  // "f1 => f2"
protected List<org.chocosolver.solver.constraints.Constraint> chocoConstraints;     // positive
protected List<org.chocosolver.solver.constraints.Constraint> negChocoConstraints;  // negative
```

Equality and hashing key **off the STRING ONLY** (not Choco internals). This is load-bearing for:
- CDR set algorithms (QuickXPlain, FastDiag) deduplicating constraints
- ConstraintUtils.containsAll, isMinimal
- LinkedHashSet-based constraint tracking in AbstractCDRModel

---

## Post → Harvest → Unpost Cycle

**Core invariant across the codebase.** The Choco Model is deliberately left holding VARIABLES ONLY; constraints live inside `Constraint` objects.

Lifecycle for each constraint add/solve/remove:

1. **Post** — `model.post(chocoConstraint)` / `model.addClauses(...)`
2. **Harvest** — Extract the Choco constraint list from `model.getCstrs()` range
3. **Unpost** — `model.unpost(harvestedList)` to restore clean state

Example from FMKB:
```java
// defineConstraints() posts all FM constraints
defineVariables();        // creates BoolVars
defineConstraints(false); // posts relationship constraints
// Harvest: ConstraintUtils.addChocoConstraintsToConstraint(...)
// Unpost: model.unpost(constraintList) — model back to variables-only state
```

**Why?** Allows multiple algorithm runs on the same KB without recreating the model. Algorithms that need specific constraint subsets re-post only what they need.

---

## Instrumentation Convention

Use `PerformanceEvaluator` (eval package, all-static global state):

```java
PerformanceEvaluator.incrementCounter("COUNTER_CONSISTENCY_CHECKS");
PerformanceEvaluator.start("TIMER_SOLVER");
PerformanceEvaluator.stop("TIMER_SOLVER");
long total = PerformanceEvaluator.total("TIMER_SOLVER");
```

Counter name constants live in `ConstraintUtils` and `CAEvaluator`. Examples: `COUNTER_POST_CONSTRAINT`, `COUNTER_UNPOST_CONSTRAINT`, `TIMER_SOLVER`.

**WARNING:** PerformanceEvaluator is NOT reentrant — it holds static maps. Parallel evaluations interfere. Also: `reset()` is rarely called and global state persists across test runs.

---

## Testing Conventions

- **Framework:** JUnit 5 (`@Test`, `@BeforeEach`, `@ParameterizedTest`, etc.)
- **File location:** `src/test/java` mirroring `src/main/java` package structure
- **Resources:** `src/test/resources` — feature models (*.sxfm, *.xml, *.json), test suites, CSP models
- **Naming:** `*Test.java`; numbered-variant pattern for multi-scenario tests (FMModelTest1..5)
- **Size:** HSDAGTest and HSTreeTest exceed 1000 LOC each; FMAnalyzerTest is 2219 LOC. No modularization threshold enforced for tests.

**Coverage gaps (by design or oversight — see merged context section 4):**
- `ca-cdr-core/translator/` — 8 files, entirely untested
- `heuristics` MF pipeline — no unit tests (dead-code indicator)
- `fm/ASTBuilder.convertToCNF` — untested; one CNF test is commented out
- `ChocoConsistencyChecker` — no dedicated unit test
- `kb/RenaultKB` / `SolveErrorTest` — test files fully commented out (pre-rename namespace)

---

## Lombok Builder Customization

Standard pattern uses `@Builder` on classes. Customized entry points:

```java
@Builder(builderMethodName="requirementBuilder")
public class Requirement extends Solution { ... }

// Usage: Requirement.requirementBuilder().assignments(...).build()
```

This is how you override the default `builder()` method name to avoid conflicts.

---

## Anti-Patterns to Avoid

**Do not repeat these:**

1. **Commented-out code in main sources** — AutomatedAnalysisBuilder has 429 lines of dead code. If code is not live, delete it or move to a branch.

2. **Global mutable static state:**
   - PerformanceEvaluator (all counters/timers static)
   - FMAssignmentsTranslator.logOpCreator (static with @Setter)
   - SolutionReader.kb (private static, assigned from constructor — second reader rebinds)
   - SolutionWriterWithCounter.counter (static, shared across instances)
   
   Instead: pass state through constructor/method params.

3. **Unchecked casts as the extension mechanism** — fm builders use `(F) new AnomalyAwareFeature(...)` casts. Use bounded generics where possible or document the cast contract.

4. **instanceof-chain dispatch** — ConfRuleTranslator's 4-way and 8-way chains. Use polymorphism or a visitor pattern instead. Adding a new AST node requires editing the chain.

5. **Catch-all returning false** — `ChocoConsistencyChecker.check()` swallows ALL exceptions and returns false. Conflates solver crash with infeasibility. Prefer `catch` with rethrow or structured error reporting.

6. **System.gc() in loops** — `HSTree.createNodes()` line 134 calls `System.gc()` per node. Let the GC decide; if memory is the issue, redesign data structures.

7. **parallelStream on tiny/ordered collections** — FeatureModel batch operations on small lists. Sequential iteration is faster and matches the intended semantics.

8. **Untyped Object params with runtime type checks** — `ITestCaseBuildable.buildTestCase(Object)`. Use generics or a dedicated builder interface.

9. **I-prefixed abstract classes** — `IConsistencyAlgorithm` is an abstract class (not an interface) that defines no algorithm method. Misleading name. Rename to `AbstractConsistencyAlgorithm` or change to an interface if truly polymorphic.

10. **Mutable method returns from getters** — `getAllConstraints()` returns a Guava view, not a copy. Caller can modify it; modifications affect the internal set unpredictably. Return unmodifiableSet instead.

---

## License Header Convention

Source files carry:

```java
/*
 * High Performance Knowledge Based Configuration Techniques
 * Copyright (c) 2022-2025 @author: Viet-Man Le
 * License: MIT
 */
```

**Note:** Year ranges are inconsistent across files (2022-2023, 2022-2024, 2023-2025). Update to current year on file modification but do not treat it as critical.

---

## Java Version & Toolchain

- **Java:** 23 (enforced in root pom: `<source>23</source> <target>23</target>`)
- **Maven:** 3.9.x+ (system-installed; NO Maven Wrapper present despite .gitignore reference)
- **Lombok:** 1.18.38 (wired via annotationProcessorPath)
- **JUnit:** 5.10.3 (test scope)
- **Choco Solver:** 4.10.14 (all modules inherit transitively)

---

## Modularization Threshold

Hand-written Java files exceeding ~300 LOC should be candidates for splitting. Current violators (12 files):

| Class | LOC |
|---|---|
| RenaultKB | 1722 |
| PCKB | 952 |
| FeatureModel | 547 |
| AutomatedAnalysisBuilder | 520 (82% commented) |
| FMKB | 439 |
| CameraKB | 433 |
| FeatureIDEParser | 385 |
| ChocoConsistencyChecker | 373 |
| Configurator | 352 |
| GLENCOEParser | 350 |
| XMIParser | 349 |
| SXFMParser | 320 |

These are tolerated where they form a natural boundary (e.g., one algorithm class, one KB subclass). Split only when it meaningfully reduces complexity.

---

## Conventional Commits

- Use conventional commit format: `type(scope): message`
- No AI references (project rule)
- Scope examples: `kb`, `ca-cdr`, `fma`, `build`, `docs`
- Types: `feat`, `fix`, `refactor`, `test`, `docs`, `chore`

Example: `fix(fma): correct anomaly type tag in FalseOptionalAnalysis`
