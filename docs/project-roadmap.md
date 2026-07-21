# HiConfiT-Core: Development Roadmap

**Current Version:** 1.0.1-alpha-48 | **Branch:** dev | **Last Updated:** 2026-07-21

## Current State Summary

**Codebase maturity:** Research-stage, feature-complete for published algorithms; development active (5 alpha bumps in last 20 commits). Test coverage uneven: fm, ca-cdr, fma well-tested; kb translators, heuristics MF pipeline, common utilities untested. CI/CD absent since 2023-05-25.

**Module health (by test coverage + LOC balance):**
- ✅ fm (42 files, 6599 LOC) — 75 @Test methods, all 5 parsers tested
- ✅ ca-cdr (35 files, 3920 LOC) — 102 @Test methods, HSTree/HSDAG coverage ~1000+ LOC each
- ✅ fma (51 files, 3492 LOC) — 51 @Test methods, FMAnalyzerTest comprehensive
- ⚠️ kb (17 files, 4592 LOC) — 2 test files (1831 LOC) 100% commented-out with pre-rename namespace
- ⚠️ heuristics (12 files, 927 LOC) — 1 test (CSV reader only); entire MF pipeline untested, no in-repo callers
- ⚠️ common (13 files, 678 LOC) — 6 tests only; 4 instances of global mutable static state
- ✅ cdrmodel (5 tested of 29 files)
- ✅ configurator (8 tests)
- ⚠️ csp2choco (1 test of 3 comparisons; grammar/impl gap silent)

---

## Priority Tiers

### P0: Correctness (Ship Blockers)

#### P0.1 FMA Anomaly-Tagging Bugs
**Status:** Open; likely tracked in local `94-fix-fma` branch.

- **FalseOptionalAnalysis.java:49** sets `AnomalyType.VOID` instead of `FALSEOPTIONAL` after consistency check
- **ConditionallyDeadAnalysis.java:48** sets `AnomalyType.DEAD` instead of `CONDITIONALLYDEAD` after consistency check

**Impact:** `isAnomalyType()` pruning and `AnomalyAwareFeature.anomalies` contents wrong; CompactExplanation reporting unaffected (uses analysis class, not tag). No test catches these (no unit test asserts `isAnomalyType(FALSEOPTIONAL)` or `isAnomalyType(CONDITIONALLYDEAD)`).

**Fix:** Correct the constants; add unit tests for each anomaly type's tagging.

#### P0.2 IntVar KB Negation Unimplemented
**Files:** ka-cdr-core/translator/KBAssignmentsTranslator.java (2 TODOs at lines ~78, ~90)

**Impact:** Negation parameter accepted, silently ignored for non-FM KBs (PCKB, RenaultKB, CameraKB). Breaks `FMRequirementCdrModel` if pointed at IntVar KB.

**Fix:** Implement negation or gate the use case; add tests for IntVar negation branch.

#### P0.3 ChocoConsistencyChecker.check() Swallows Exceptions
**File:** ca-cdr-package/checker/ChocoConsistencyChecker.java:310

**Code:** `catch (Exception e) { return false; }` — Choco solver crash, infeasibility, and timeout all map to false.

**Impact:** Errors silently become false. If Choco throws (e.g., out of memory, assertion), diagnosis becomes unreliable.

**Fix:** Distinguish solver errors from infeasibility; log exceptions; consider propagating solver-side errors.

---

### P0: Documentation Accuracy (Reader Trust)

#### P0.4 README Incomplete/Inaccurate
**Issues (from merged context section 6.5):**

1. ~~**WipeOutR_T marked "implemented", but does not exist.**~~ **RESOLVED 2026-07-21** — maintainer confirmed the algorithm ships from the separate [AIG-ist-tugraz/WipeOutR](https://github.com/AIG-ist-tugraz/WipeOutR) repo, not this one. README entry now carries that pointer. The in-repo hook (`ChocoConsistencyChecker.isConsistent(tc, neg_tc)`, line 142) remains, with no in-repo consumer — see Open Questions.

2. ~~**Version table claims `1.0` for all 11 artifacts.**~~ **RESOLVED 2026-07-21** — table updated to `1.0.1-alpha-48`. Note this table must now be re-edited on every release, since `artifact.version` is already duplicated with `<version>` in the root pom (see P1.5).

3. ~~**Documentation URL conflict.**~~ **RESOLVED 2026-07-21** — `https://hiconfit.github.io` confirmed canonical; README updated, docs aligned.

4. **Omits composition layer:** README lists 9 CDR algorithms but omits the 6 labeler + 2 pruning-engine composition surface (the actual extension point). Confuses readers about how HSDAG/HSTree are extended. **STILL OPEN.**  
   **Fix:** Add subsection "Composition & Extension" describing IHSLabelable, labelers, pruning engines.

#### P0.5 CLAUDE.md Dependency Graph Errors
**Files:** CLAUDE.md sections "Module Dependencies" and "Layer 1"

**Verified errors:**
1. "eval has no deps within project" — FALSE; eval depends on common (eval-package/pom.xml:18)
2. "csp2choco feeds kb layer" — FALSE; kb depends on fm + eval; csp2choco enters at cdrmodel layer (cdrmodel-package/pom.xml:50)
3. "FMCdrModel cross-product with test cases" — UNSUPPORTED; no such logic in codebase (repo-wide grep returns nothing)

**Fix:** Correct dependency graph, remove "cross-product" claim, cite merged-scout-context section 1 as source.

---

### P1: Build & Release Integrity

#### P1.1 ANTLR Plugin Mis-Scoped (Silent Grammar Bit-Rot)
**Files:** csp2choco-package/pom.xml:32–43, fm-package/pom.xml:44–56

**Issue:** `antlr4-maven-plugin:4.13.1` declared in `<dependencies>`, NOT `<build><plugins>`. No `<build>` section exists. Consequence: ANTLR never runs at build time; `.g4` files are inert. The 5 generated Java files (1732 LOC) are hand-committed, headers reference ANTLR 4.12.0 and an obsolete absolute path.

**Three ANTLR versions coexist:** 4.12.0 (csp2choco generated), 4.13.1 (plugin), 4.13.2 (fm4conf/gen/).

**Fix:**
1. Move plugin to `<build><plugins>` in both poms
2. Regenerate `.g4` files; verify 1-version uniformity
3. Decide: grammars as source of truth (regen on build) or hand-maintenance (remove plugin, document manual steps)

#### P1.2 No CI/CD Pipeline; Manual Release Process
**Status:** CI removed 2023-05-25 (commits 397ed7d, b014c0a deleted `.github/workflows/`).

**Current:** Release = manual `mvn deploy` with GitHub credentials in `~/.m2/settings.xml`.

**Risk:** No automated test run on push/PR; release errors not caught; Dependabot absent.

**Fix:** Reinstate `.github/workflows/maven-publish.yml` and optionally a test-on-push workflow.

#### P1.3 Build Fragility (No Wrapper, No Dockerfile, No Shell Scripts)
**Files:** root, `mvnw` / `.mvn/` missing despite `.gitignore` reference

**Issue:** Build depends on system-installed Maven 3.x + JDK 23. Not reproducible on other machines/containers.

**Fix (priority contingent on use case):**
1. Add Maven Wrapper: `mvn wrapper:wrapper`
2. (Optional) Add Dockerfile for build/test standardization
3. Document JDK 23 requirement prominently

#### P1.4 Dependency Management & Plugin Overhead
**Issue:** No `<dependencyManagement>` or `<pluginManagement>` at root. Versions duplicated:
- maven-assembly-plugin 3.3.0 appears in 3 module poms
- antlr4-maven-plugin versions skew (4.13.1 vs 4.13.2)
- maven-surefire-plugin 2.22.0 (2018-era; current 3.5.x) — should upgrade for Java 23 compat

**Fix:** Centralize versions in root pom `<dependencyManagement>` + `<pluginManagement>`. Upgrade surefire to 3.x.

#### P1.5 artifact.version Duplication
**File:** root pom.xml:4 (property) and root pom.xml:3 (version tag)

**Issue:** Must bump both in lockstep; prone to divergence on manual release.

**Fix:** Use property interpolation: `<version>${artifact.version}</version>` (if root artifact version should be dynamic), or remove property if unused downstream.

---

### P1: Dependency Risk

#### P1.6 Mahout Ancient & Mismatched
**Dependency:** mahout-core 0.9 (2014) + mahout-math 0.13.0 (pre-release, mismatched pair) in heuristics-package/pom.xml:20–21

**Risk:** Oldest dependency tree-wide; pre-2020 release; pom comment states "there is a conflict if upgrade this" without elaboration.

**Impact:** No in-repo callers; MF half of heuristics has no tests, no consumers.

**Decision needed:** Is MF half consumed by downstream repo? If not, consider deletion. If yes, modernize Mahout (currently 0.13.0 → consider 1.0+) and document the conflict.

#### P1.7 Transitive Vulnerability Suppression
**File:** fma-package/pom.xml:23 — `<!--suppress VulnerableLibrariesLocal -->`

**Issue:** A transitive dependency carries a known CVE; suppression applied rather than fixed.

**Fix:** Identify the CVE, upgrade the transitive dependency, or document the risk/mitigation.

---

### P2: Test Coverage Gaps

| Surface | Files | Scope | Impact |
|---|---|---|---|
| **ca-cdr-core translator/** | 8 files | All translation logic (IAssignmentsTranslatable, ISolutionTranslatable, ITestCaseTranslatable impls, LogOpCreator) | High: core SPI layer |
| **heuristics MF pipeline** | MFVVOHeuristic, MFVVOModel, MatrixFactorization, both selectors | No in-repo callers; untested pipeline | Medium: dead code risk |
| **fm ASTBuilder.convertToCNF** | fm/core/ast/ASTBuilder.java | CNF conversion (most intricate logic); only test commented out | High: active code, risky |
| **fm builder/** | 6 files + FeatureModels, ConfRuleTranslator, FMFormat, FMParserFactory | Builder instantiation, factory selection, translator dispatch | Medium: extension seam |
| **kb RenaultKB / SolveErrorTest** | 1831 commented LOC in kb test files | Zero live assertions; pre-rename namespace (would not compile if uncommented) | Low: appears legacy |
| **ChocoConsistencyChecker** | checker/ChocoConsistencyChecker.java:373 LOC | Clone-model paths, WipeOutR_T hook (isConsistent(tc, neg_tc)), 5 overloads | High: core instrument |
| **fma explanation/** | 5 explanators + utils, 7 of 8 builders, assumption gen, monitor | No direct unit tests; 5 explanators are byte-identical | Medium: likely duplication opportunity |
| **common utils** | ChocoSolverUtils, LoggerUtils, MailService, cfg/*, ThreadUtils, cli/* | All support code; 4 global mutable static fields | Low: tested indirectly |

**Quick wins:** Write ChocoConsistencyChecker unit tests covering clone-model + WipeOutR_T hook; migrate kb test files to current namespace or delete.

---

### P2: Dead Code & Modularization

#### P2.1 AutomatedAnalysisBuilder Dead Code
**File:** fma-package/builder/AutomatedAnalysisBuilder.java (520 LOC, **429 lines commented — 82% dead**)

**Action:** Audit commented code; delete if stale. If transitional or WIP, extract to a branch or mark as TODOs.

#### P2.2 Duplicate FMA Explanators
**Files:** 5 explanator classes (~50 LOC each in fma/explanator/); byte-identical except class name

**Action:** Merge to one generic DirectDebugExplanator; use factory/strategy for per-type wiring.

#### P2.3 Hardcoded KB Data Over 1000 LOC
| File | LOC | Content |
|---|---|---|
| RenaultKB | 1722 | 100+ hardcoded domains + inline rule list |
| PCKB | 952 | ~30 hardcoded domains |
| CameraKB | 433 | Explicit chocoValues |

**Action:** Extract data to external format (JSON, XML, CSV); load via parser. Reduces LOC, improves reusability.

#### P2.4 Stray Generated Files Not Gitignored
**Files:** fm-package/gen/, fm4conf/gen/ (ANTLR regeneration byproducts)

**Action:** Add to `.gitignore`; delete from repo history (optional: `git rm --cached`).

#### P2.5 Global Mutable Static State (4 instances)
| Class | Field | Risk |
|---|---|---|
| PerformanceEvaluator | counters, timers (ConcurrentHashMap), showEvaluation (public mutable) | Makes evaluation non-reentrant; concurrent tests interfere |
| FMAssignmentsTranslator | logOpCreator (static @Setter) | Changes behaviour process-wide |
| SolutionReader | kb (private static from instance constructor) | Second reader rebinds first |
| SolutionWriterWithCounter | counter (static) | Shared across all writers/folders |

**Action:** Thread-scope or instance-scope these fields; or document concurrency contract explicitly.

---

### P3: Planned Features (From README "Coming Soon")

| Algorithm | Status | Reference | Notes |
|---|---|---|---|
| AggregatedTest | Unimplemented | README:41 / [14] | Test case aggregation for efficiency |
| LevelWiseParallelHSDAG | Unimplemented | README:42 / [10,11] | Parallel hitting set construction |
| FullParallelHSDAG | Unimplemented | README:43 / [10,11] | Full parallelization variant |
| FastDiagP | Unimplemented | README:44 / [15] | Parallelized direct diagnosis; Python impl exists (external) |
| KBDiag | Unimplemented | README:45 | KB-specific diagnosis; stale trace in DirectDebugLabeler.java:61,93 |
| InformedQX | Unimplemented | README:46 | Informed QuickXPlain variant |
| ParallelWipeOutR_T | Unimplemented | README:47 | Parallel redundancy (assumes WipeOutR_T exists — currently doesn't) |
| ParallelWipeOutR_FM | Unimplemented | README:48 | Parallel redundancy for FM |

**Prioritization contingent on research milestones.** KBDiag stale trace (lines 61, 93 of DirectDebugLabeler) should be cleaned up or implemented.

---

## Open Questions for Maintainers

These require human judgment and are tracked for future decision-making:

1. **WipeOutR_T intent?** Intended elsewhere, or README entry mis-marked?
2. **FastDiag.java (deprecated, 255 LOC, untested, unreferenced)?** Keep for binary compatibility, or delete?
3. **heuristics MF half consumed downstream?** No in-repo callers; critical for roadmapping.
4. **System.gc() in HSTree.createNodes():134?** Deliberate memory mitigation, or leftover debugging?
5. **Configurator.clearVVO() broken state restore?** Commented-out `solver.setSearch(defaultSearch)` intentional?
6. **ANTLR grammars: source of truth or manual?** Plugin mis-scoping suggests accidental; 3-version skew confirms.
7. **fm-package/gen/ + fm4conf/gen/ stray trees?** Intended artifacts or interrupted regeneration?
8. **CSP2Choco.g4 unimplemented portions?** Planned work or dead grammar surface (requirement, connectives, arithmetic)?
9. **94-fix-fma branch tracking?** Targets the two anomaly-tag bugs? Status?
10. **GitHub CI deletion deliberate?** Moved to org-level workflows, or abandoned?
11. **fm.gen.version / kbstatistic.version unused?** Inherited by sibling repo, or orphaned properties?
12. **SolveErrorTest.java / RenaultKBTest.java (1831 commented LOC)?** Delete or migrate to current namespace?
13. **mahout-core 0.9 / mahout-math 0.13.0 conflict reproducible?** Still necessary, or safe to modernize?
14. **AbstractCDRModel dead `Set<String>` mirrors?** Remove, or planned for future use?

---

**Next:** File issues from P0 & P1 tiers; use P2 for backlog grooming; P3 for research planning.

**See also:** [project-overview-pdr.md](./project-overview-pdr.md) for functional/non-functional requirements; README.md for references.
