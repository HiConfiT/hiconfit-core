# Plan — Injectable split point for QuickXPlain (CC #1, hiconfit-core)

Status: **PHASE A — awaiting approval. No production code written.**
Date: 2026-07-21 · Branch: `dev` · Repo: `hiconfit-core` only.

WHAT/WHY brief (authoritative, do not duplicate):
`~/Library/Mobile Documents/iCloud~md~obsidian/Documents/Everything/Cowork/ML4QX/design-learned-split-ratio.md`
This plan is the HOW, verified against current source. Brief wins on intent; where the brief
contradicts the actual code, the discrepancy is recorded in §5 and the code wins on fact.

Goal: generalize QuickXPlain's split point from hard-coded midpoint to an injectable
`SplitPointStrategy`. Default behaviour bit-identical to today. hiconfit-core stays
probability-agnostic — the word "probability" must not appear in any file this plan touches.

---

## 0. TL;DR — two findings that change the brief's code

**F1 (blocker).** The brief's unconditional clamp `k = max(1, min(k, n-1))` in the shared
`split` **regresses the existing no-arg overload** for `n < 2`:
`n==0` → `subList(0,1)` on an empty list → `IndexOutOfBoundsException`;
`n==1` → halves swap (`C1=[c1],C2=[]` instead of legacy `C1=[],C2=[c1]`).
No current in-repo caller hits it (§4 — all 6 guard `n>=2`), but `split` is published public API.
Fix: clamp conditionally. See §2.2.

**F2 — RESOLVED by brief revision (§5.1, §11), 2026-07-21.** Originally `Math.round` made
`RatioSplitPointStrategy(0.5) != MIDPOINT` on odd sizes (`round(0.5*5)=3` vs `5/2=2`), which
would have broken brief §1/§7.2's "r = 0.5 reproduces the current algorithm exactly".

**Brief now specifies `Math.floor`.** `floor(0.5*n) == n/2` for every n ≥ 0, so
`RatioSplitPointStrategy(0.5)` is byte-identical to `MIDPOINT` at all sizes. Consequences:
no divergence test to pin, and no Contract A caveat — CC #2's `0.5 / true` default per §7.2
is exactly midpoint and needs no special-casing.

---

## 1. Files to touch

| # | File | Action |
|---|---|---|
| 1 | `kb-package/src/main/java/at/tugraz/ist/ase/hiconfit/common/SplitPointStrategy.java` | CREATE |
| 2 | `kb-package/src/main/java/at/tugraz/ist/ase/hiconfit/common/RatioSplitPointStrategy.java` | CREATE |
| 3 | `kb-package/src/main/java/at/tugraz/ist/ase/hiconfit/common/ConstraintUtils.java` | MODIFY (add overload, delegate old) |
| 4 | `ca-cdr-package/src/main/java/at/tugraz/ist/ase/hiconfit/cacdr/algorithms/QuickXPlain.java` | MODIFY (fields, setter, thread through `qx`) |
| 5 | `ca-cdr-package/src/main/java/at/tugraz/ist/ase/hiconfit/cacdr/algorithms/hs/labeler/QuickXPlainLabeler.java` | MODIFY (carry strategy into clones — decided) |
| 6 | `pom.xml` + 11 module `pom.xml` | MODIFY (version bump — decided) |
| 7 | `kb-package/src/test/java/at/tugraz/ist/ase/hiconfit/common/SplitPointStrategyTest.java` | CREATE (tests) |
| 8 | `ca-cdr-package/src/test/java/at/tugraz/ist/ase/hiconfit/cacdr/algorithms/QuickXPlainSplitStrategyTest.java` | CREATE (tests) |

Not touched: the 5 other split callers, any `FastDiag*` (decided: QuickXPlain only this round).

---

## 2. Exact diffs (quoted against current source)

### 2.1 NEW — `SplitPointStrategy.java`

Package `at.tugraz.ist.ase.hiconfit.common` (kb-package), next to `ConstraintUtils`.
Note `ConstraintUtils` is `@UtilityClass`, so it cannot host a nested interface cleanly —
separate files are correct. Per-file header matches repo convention.

```java
/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2026
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.common;

/**
 * Determines the split point k used by divide-and-conquer consistency algorithms
 * when partitioning a constraint set into two halves.
 * <p>
 * Deliberately takes only the set size: every strategy shipped here needs nothing more,
 * and widening the signature would be speculative generality.
 */
@FunctionalInterface
public interface SplitPointStrategy {

    /**
     * @param size the size of the constraint set being split at the current call
     * @return the split point k; callers clamp to [1, size-1] where a real split is possible
     */
    int computeK(int size);

    /** Midpoint split — reproduces the historical hard-coded behaviour. */
    SplitPointStrategy MIDPOINT = size -> size / 2;
}
```

### 2.2 NEW — `RatioSplitPointStrategy.java`

```java
/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2026
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.common;

import lombok.Getter;

/**
 * Splits at a fixed fraction of the set size: k = floor(ratio * size).
 * <p>
 * {@code ratio} is a plain scalar. This class attaches no meaning to it and does not
 * know how it was derived; callers own that.
 * <p>
 * Flooring makes ratio 0.5 exactly equivalent to {@link SplitPointStrategy#MIDPOINT} at every
 * size, since {@code floor(0.5 * size) == size / 2}.
 */
@Getter
public final class RatioSplitPointStrategy implements SplitPointStrategy {

    private final double ratio;

    public RatioSplitPointStrategy(double ratio) {
        this.ratio = ratio;
    }

    @Override
    public int computeK(int size) {
        return (int) Math.floor(ratio * size);
    }
}
```

`@Getter` added beyond the brief so CC #2 can log/assert the injected ratio. Drop if unwanted.

### 2.3 MODIFY — `ConstraintUtils.java`

CURRENT, **lines 199–216** (brief says "around line 207"; actual method starts at **206**):

```java
    /**
     * Split a set of {@link Constraint}s into two sets
     *
     * @param C an input set of {@link Constraint}s
     * @param C1 the first output set - needs to be initialized
     * @param C2 the second output set - needs to be initialized
     */
    public void split(Set<Constraint> C, Set<Constraint> C1, Set<Constraint> C2) {
        int k = C.size() / 2; // k = sizeC/2;
        // C1 = {c1..ck}; C2 = {ck+1..cn};
        List<Constraint> firstSubList = new ArrayList<>(C).subList(0, k);
        List<Constraint> secondSubList = new ArrayList<>(C).subList(k, C.size());

        C1.addAll(firstSubList);
        C2.addAll(secondSubList);

        incrementCounter(COUNTER_SPLIT_SET);
    }
```

PROPOSED — note the clamp is **conditional**, diverging from brief §6.2 to fix F1:

```java
    /**
     * Split a set of {@link Constraint}s into two sets at a given split point k.
     * <p>
     * When a real split is possible (size >= 2) k is clamped to [1, size-1] so that both
     * halves are non-empty and divide-and-conquer recursion terminates. For size < 2 no
     * split is possible and the historical behaviour is preserved exactly.
     *
     * @param C an input set of {@link Constraint}s
     * @param C1 the first output set - needs to be initialized
     * @param C2 the second output set - needs to be initialized
     * @param k the split point
     */
    public void split(Set<Constraint> C, Set<Constraint> C1, Set<Constraint> C2, int k) {
        int n = C.size();
        // keep both halves non-empty where a split exists; below that, preserve legacy behaviour
        k = (n >= 2) ? Math.max(1, Math.min(k, n - 1)) : Math.max(0, Math.min(k, n));

        // C1 = {c1..ck}; C2 = {ck+1..cn};
        List<Constraint> list = new ArrayList<>(C);
        C1.addAll(list.subList(0, k));
        C2.addAll(list.subList(k, n));

        incrementCounter(COUNTER_SPLIT_SET);
    }

    /**
     * Split a set of {@link Constraint}s into two equal halves.
     *
     * @param C an input set of {@link Constraint}s
     * @param C1 the first output set - needs to be initialized
     * @param C2 the second output set - needs to be initialized
     */
    public void split(Set<Constraint> C, Set<Constraint> C1, Set<Constraint> C2) {
        split(C, C1, C2, C.size() / 2);
    }
```

Verified invariants:
- `public void` (not `static`) — `@UtilityClass` at line 27 makes members static. Writing
  `static` explicitly would be redundant and break house style.
- `COUNTER_SPLIT_SET` incremented exactly once per call, unchanged. Declared at
  **`ConstraintUtils.java:35`**; the `CAEvaluator` copy is commented out (`CAEvaluator.java:36`) —
  tests must read it from `ConstraintUtils`.
- Two `new ArrayList<>(C)` allocations collapse to one. Same content, no behavioural change.
- Legacy equivalence table for the no-arg overload:

| n | legacy k | new k | C1/C2 | same? |
|---|---|---|---|---|
| 0 | 0 | 0 | `[]` / `[]` | yes |
| 1 | 0 | 0 | `[]` / `[c1]` | yes |
| 2 | 1 | 1 | `[c1]` / `[c2]` | yes |
| 5 | 2 | 2 | `[c1,c2]` / `[c3,c4,c5]` | yes |

(With the brief's unconditional clamp, rows n=0 and n=1 break. Hence §2.3's conditional form.)

### 2.4 MODIFY — `QuickXPlain.java`

**(a) import** — add after line 12 (`import ...common.ConstraintUtils;`):

```java
import at.tugraz.ist.ase.hiconfit.common.SplitPointStrategy;
```

**(b) fields + setter** — insert after the constructor (current lines 59–61):

```java
    // MIDPOINT + applyAtAllLevels = true  =>  identical to the historical midpoint QuickXPlain
    private SplitPointStrategy splitStrategy = SplitPointStrategy.MIDPOINT;
    private boolean applyAtAllLevels = true;

    /**
     * Sets the split-point strategy used when partitioning the consideration set.
     *
     * @param splitStrategy the strategy; {@link SplitPointStrategy#MIDPOINT} restores default behaviour
     * @param applyAtAllLevels if true the strategy drives every recursion level;
     *                         if false only the top-level split, midpoint below
     */
    public void setSplitStrategy(@NonNull SplitPointStrategy splitStrategy, boolean applyAtAllLevels) {
        this.splitStrategy = splitStrategy;
        this.applyAtAllLevels = applyAtAllLevels;
    }
```

`@NonNull` is already imported (line 16) and matches house style.

**(c) top-level call** — CURRENT **line 89**:

```java
            Set<Constraint> cs = qx(Collections.emptySet(), C, B);
```
PROPOSED:
```java
            Set<Constraint> cs = qx(Collections.emptySet(), C, B, splitStrategy);
```

**(d) `qx` signature** — CURRENT **line 114**:

```java
    private Set<Constraint> qx(Set<Constraint> D, Set<Constraint> C, Set<Constraint> B) {
```
PROPOSED (+ one `@param` line in the existing javadoc block at lines 109–112):
```java
    private Set<Constraint> qx(Set<Constraint> D, Set<Constraint> C, Set<Constraint> B,
                               SplitPointStrategy strategy) {
```

**(e) split call** — CURRENT **lines 138–141**:

```java
        // C1 = {c1..ck}; C2 = {ck+1..cq};
        Set<Constraint> C1 = new LinkedHashSet<>();
        Set<Constraint> C2 = new LinkedHashSet<>();
        ConstraintUtils.split(C, C1, C2);
```
PROPOSED:
```java
        // C1 = {c1..ck}; C2 = {ck+1..cq};
        Set<Constraint> C1 = new LinkedHashSet<>();
        Set<Constraint> C2 = new LinkedHashSet<>();
        int k = strategy.computeK(q);           // split() clamps to [1, q-1]
        ConstraintUtils.split(C, C1, C2, k);
```
Reuses the local `q` already computed at line 130 (`int q = C.size();`) — same value as
`C.size()`, avoids a redundant call.

**(f) recursive calls** — CURRENT **lines 144–154**:

```java
        // CS1 <-- QX(C2, C1, B ∪ C2);
        Set<Constraint> BwithC2 = Sets.union(B, C2); incrementCounter(COUNTER_UNION_OPERATOR);
        incrementCounter(COUNTER_LEFT_BRANCH_CALLS);
        incrementCounter(COUNTER_QUICKXPLAIN_CALLS);
        Set<Constraint> CS1 = qx(C2, C1, BwithC2);

        // CS2 <-- QX(CS1, C2, B ∪ CS1);
        Set<Constraint> BwithCS1 = Sets.union(B, CS1); incrementCounter(COUNTER_UNION_OPERATOR);
        incrementCounter(COUNTER_RIGHT_BRANCH_CALLS);
        incrementCounter(COUNTER_QUICKXPLAIN_CALLS);
        Set<Constraint> CS2 = qx(CS1, C2, BwithCS1);
```
PROPOSED (only the two `qx(...)` args change; every counter line untouched):
```java
        SplitPointStrategy childStrategy = applyAtAllLevels ? strategy : SplitPointStrategy.MIDPOINT;

        // CS1 <-- QX(C2, C1, B ∪ C2);
        Set<Constraint> BwithC2 = Sets.union(B, C2); incrementCounter(COUNTER_UNION_OPERATOR);
        incrementCounter(COUNTER_LEFT_BRANCH_CALLS);
        incrementCounter(COUNTER_QUICKXPLAIN_CALLS);
        Set<Constraint> CS1 = qx(C2, C1, BwithC2, childStrategy);

        // CS2 <-- QX(CS1, C2, B ∪ CS1);
        Set<Constraint> BwithCS1 = Sets.union(B, CS1); incrementCounter(COUNTER_UNION_OPERATOR);
        incrementCounter(COUNTER_RIGHT_BRANCH_CALLS);
        incrementCounter(COUNTER_QUICKXPLAIN_CALLS);
        Set<Constraint> CS2 = qx(CS1, C2, BwithCS1, childStrategy);
```

No counter increment is added, removed, moved, or reordered anywhere. CC comparisons stay fair.

### 2.5 MODIFY — `QuickXPlainLabeler.java` (decided: fix now)

`getInstance` is HSDAG/HSTree's clone-for-parallel hook. It currently rebuilds the labeler without
copying the new fields, so a strategy set on the original silently reverts to MIDPOINT in clones.

CURRENT, **lines 93–95**:

```java
    public IHSLabelable getInstance(@NonNull ChocoConsistencyChecker checker) {
        return new QuickXPlainLabeler(checker, this.initialParameters);
    }
```
PROPOSED:
```java
    public IHSLabelable getInstance(@NonNull ChocoConsistencyChecker checker) {
        QuickXPlainLabeler instance = new QuickXPlainLabeler(checker, this.initialParameters);
        // clones must inherit the split policy, otherwise they silently revert to midpoint
        instance.setSplitStrategy(getSplitStrategy(), isApplyAtAllLevels());
        return instance;
    }
```

Requires two accessors on `QuickXPlain` alongside the setter from §2.4(b) — `getSplitStrategy()`
and `isApplyAtAllLevels()`. Add them explicitly rather than via `@Getter`: `QuickXPlainLabeler`
is annotated `@Getter` at class level (line 29) and `QuickXPlain` is not, so a field-level Lombok
annotation here would be inconsistent with the surrounding style.

Behaviour is unchanged for every existing user: defaults are `MIDPOINT / true`, so clones inherit
exactly what they already got implicitly. No test in `HSDAGTest` / `HSTreeTest` changes.

New test in §3.2: `labelerClonesInheritSplitStrategy`.

---

## 3. Unit tests

Counters read via `PerformanceEvaluator.getCounter(name).getValue()` (returns `long`,
`Counter.java:18` `@Getter`). `CAEvaluator.reset()` before every measured run — the evaluator is
global static state, so tests are order-sensitive if this is skipped.

### 3.1 `SplitPointStrategyTest` (kb-package) — pure, no solver

| Test | Asserts |
|---|---|
| `midpointMatchesLegacyHalving` | `MIDPOINT.computeK(n) == n/2` for n = 0..10 |
| `ratioComputesFlooredFraction` | `RatioSplitPointStrategy(0.25).computeK(8) == 2`; `(0.75).computeK(8) == 6`; `(0.3).computeK(10) == 3` |
| `ratioAtHalfEqualsMidpointForAllSizes` | **pins F2 resolution**: `(0.5).computeK(n) == MIDPOINT.computeK(n)` for n = 0..20, odd and even |
| `splitWithExplicitKPartitionsAtK` | `split(C,C1,C2,2)` on 5 constraints → C1 = first 2, C2 = last 3, order preserved |
| `splitClampsToNonEmptyHalves` | k = -5, 0, 1, n-1, n, 999 on n = 5 → both halves always non-empty; k=0→1, k=999→4 |
| `splitNoArgIsUnchangedForLegacySizes` | for n = 0,1,2,3,5,8: no-arg `split` yields exactly the pre-change partition (**regression guard for F1**); n=0 and n=1 must not throw |
| `splitIncrementsCounterOncePerCall` | `COUNTER_SPLIT_SET` (from `ConstraintUtils`) +1 per call, both overloads |

### 3.2 `QuickXPlainSplitStrategyTest` (ca-cdr-package) — mirrors `QuickXplainTest`

Fixtures: `TestModel1..5` from `cdrmodel.test_model.model`, same as the existing
`QuickXplainTest` (which asserts `firstConflictSet == testModel.getExpectedFirstConflict()`).

| Test | Asserts | Brief ref |
|---|---|---|
| `defaultsReproduceBaselineConflict` | untouched `new QuickXPlain(checker)` returns `getExpectedFirstConflict()` on TestModel1..5 | §10.3 |
| `explicitMidpointStrategyReproducesBaselineConflict` | `setSplitStrategy(MIDPOINT, true)` returns the identical conflict set as the untouched instance | §10.3 |
| `explicitMidpointStrategyReproducesBaselineCounters` | with `MIDPOINT/true`, all of `COUNTER_SPLIT_SET`, `COUNTER_CONSISTENCY_CHECKS`, `COUNTER_QUICKXPLAIN_CALLS`, `COUNTER_LEFT_BRANCH_CALLS`, `COUNTER_RIGHT_BRANCH_CALLS`, `COUNTER_UNION_OPERATOR` equal the untouched-instance values exactly | §10.3 |
| `ratioStrategiesReturnSameConflictAsMidpoint_allLevels` | r ∈ {0.1, 0.25, 0.4, 0.6, 0.75, 0.9}, `applyAtAllLevels=true` → conflict set equals MIDPOINT result on TestModel1..5 | §10.2 |
| `ratioStrategiesReturnSameConflictAsMidpoint_topLevelOnly` | same ratios, `applyAtAllLevels=false` → same conflict set | §10.2, §4 config 2 |
| `extremeRatiosTerminateAndStayCorrect` | r ∈ {0.001, 0.999} → terminates (no SO/hang), conflict set still equals MIDPOINT result | §10.2, clamp |
| `ratioChangesSplitCountButNotResult` | r=0.9 vs MIDPOINT: conflict sets equal, and `COUNTER_SPLIT_SET`/`COUNTER_CONSISTENCY_CHECKS` are *recorded* (not asserted equal) — proves the knob actually reaches the algorithm | §4 |
| `topLevelOnlyDiffersFromAllLevelsBelowTop` | with a strongly skewed r, `applyAtAllLevels` true vs false produce different `COUNTER_SPLIT_SET`/CC while returning the same conflict — proves the flag is wired | §4 |
| `labelerClonesInheritSplitStrategy` | `QuickXPlainLabeler.setSplitStrategy(Ratio(0.8), false)` → `getInstance(checker)` clone reports the same strategy and flag (§2.5 regression guard) | §2.5 |

Last two matter: without them, a strategy silently ignored inside `qx` would still pass every
correctness test. They assert the mechanism is *live*, not just harmless.

---

## 4. Callers that could be affected (all verified)

`ConstraintUtils.split(C,C1,C2)` — **6 call sites**, all keep working via the delegate:

| Caller | Line | Guard before split | Safe? |
|---|---|---|---|
| `QuickXPlain` | 141 | `if (q == 1) return C;` (line 131) | yes — rewritten to 4-arg |
| `FastDiagV2` | 150 | `if (q == 1) return C;` | yes — untouched |
| `FastDiagV3` | 149 | `if (n == 1) return ∅;` | yes — untouched |
| `DirectDiag` | 143 | `if (n == 1) return C;` | yes — untouched |
| `DirectDebug` | 163 | `if (n == 1) return ∅;` | yes — untouched |
| `FlexDiag` | 146 | `if (q <= m) return S;`, m ≥ 1 ⇒ q ≥ 2 | yes — untouched |

Every one guarantees `n >= 2` at the split call, so none is exposed to F1 today. The conditional
clamp protects external/published callers and future ones.

`QuickXPlain.qx(...)` — **private**, single caller (`findConflictSet`, line 89). No external impact.

`QuickXPlain` subclasses — **`QuickXPlainLabeler`** (`hs/labeler/QuickXPlainLabeler.java:30`)
`extends QuickXPlain`. It calls `findConflictSet` (line 61) so it inherits the new behaviour
correctly. **But `getInstance(checker)` (lines 93–95) does
`new QuickXPlainLabeler(checker, this.initialParameters)` — it does not copy `splitStrategy`.**
HSDAG/HSTree use `getInstance` as the clone-for-parallel hook, so a strategy set on a labeler is
silently lost in clones, reverting to MIDPOINT. Brief §8d declares the labeler/HSDAG path out of
scope for the experiment, so this is latent, not active. See Q3 — 2-line fix, not doing it unasked.

---

## 5. Discrepancies: brief vs. actual code

| # | Brief says | Actual code | Impact |
|---|---|---|---|
| D1 | §6.2 clamp `k = max(1, min(k, n-1))` unconditionally | breaks no-arg overload at n=0 (exception) and n=1 (halves swap) | **blocker** — plan uses a conditional clamp (§2.3) |
| D2 | ~~§5.1 `Math.round`~~ → brief revised to `Math.floor` (§5.1, §11) | `floor(0.5*n) == n/2` for all n | **resolved** — §1/§7.2's claim now holds; no Contract A caveat |
| D3 | §6.4 "FastDiag does not use `ConstraintUtils.split`; it inlines `int k = cSize/2` (FastDiag.java:77) … the FastDiag family / FlexDiag / DirectDiag likely each do the same" | Only the **`@Deprecated`** `FastDiag.java:77` inlines. `FastDiagV2`, `FastDiagV3`, `FlexDiag`, `DirectDiag`, `DirectDebug` **all** call `ConstraintUtils.split` | stretch goal §6.4 is far cheaper than assumed — 5 algorithms already funnel through one seam. Q4 |
| D4 | §6.2 "`split`, around line 207" | method at **206** | cosmetic |
| D5 | §6.3 line refs 89 / 114 / 138-141 / 148 / 154 | all accurate | none |
| D6 | — (not mentioned) | `COUNTER_SPLIT_SET` is declared in `ConstraintUtils:35`; the `CAEvaluator:36` copy is commented out | tests must import from `ConstraintUtils` |
| D7 | — (not mentioned) | `ConstraintUtils` is `@UtilityClass`; members are `public void`, implicitly static | brief's snippets are already correct; do not add `static` |

---

## 5b. Agnosticism check

Grep gate, run at end of Phase B, must return zero:
```bash
grep -rniE "probab|prior|posterior|likelihood|predict" \
  kb-package/src/main/java/at/tugraz/ist/ase/hiconfit/common/SplitPointStrategy.java \
  kb-package/src/main/java/at/tugraz/ist/ase/hiconfit/common/RatioSplitPointStrategy.java \
  kb-package/src/main/java/at/tugraz/ist/ase/hiconfit/common/ConstraintUtils.java \
  ca-cdr-package/src/main/java/at/tugraz/ist/ase/hiconfit/cacdr/algorithms/QuickXPlain.java
```
`RatioSplitPointStrategy` javadoc deliberately says "plain scalar … attaches no meaning to it".

---

## 6. Build, test, install

```bash
# focused build + tests (builds common, eval, fm, csp2choco, kb, ca-cdr-core, cdrmodel, ca-cdr)
mvn clean install -pl kb-package,ca-cdr-package -am

# narrowest first during development
mvn test -pl kb-package     -Dtest=SplitPointStrategyTest
mvn test -pl ca-cdr-package -Dtest=QuickXPlainSplitStrategyTest
# regression guard for the 5 untouched split callers
mvn test -pl ca-cdr-package -Dtest='QuickXplainTest,FastDiagV2Test,FastDiagV3Test,FlexDiagTest,DirectDiagTest,DirectDebugTest'

# full reactor before handing Contract A to CC #2
mvn clean install
```

No Maven Wrapper in this repo — system Maven + JDK 23 required.

**DECIDED 2026-07-21: bump `1.0.1-alpha-48` → `1.0.1-alpha-49`.** Makes the handoff to CC #2
explicit instead of relying on an invisible same-version reinstall.

Blast radius — **13 literal edits**, all mechanical:

| Location | Count | What |
|---|---|---|
| `pom.xml:18` | 1 | root `<version>` |
| `pom.xml:25` | 1 | root `<artifact.version>` property |
| `*/pom.xml` | 11 | each module's `<parent><version>` (one occurrence per pom) |

Inter-module dependency versions already use `${artifact.version}` (12 sites across 8 poms) so
they follow automatically — that is exactly what the property exists for. Verify with:

```bash
grep -rn "1.0.1-alpha-48" pom.xml */pom.xml   # must return zero after the bump
mvn -q validate                                # reactor resolves with the new version
```

**Artifact coordinates installed to `~/.m2` (verified from poms, NOT from README):**

| groupId | artifactId | version |
|---|---|---|
| `at.tugraz.ist.ase.hiconfit` | `kb` | `1.0.1-alpha-49` |
| `at.tugraz.ist.ase.hiconfit` | `ca-cdr` | `1.0.1-alpha-49` |

CC #2 must update their dependency version to `-49`.

Note the README's install table shows groupId `at.tugraz.ist.ase` (no `.hiconfit`) — that is a
**README defect**, already logged in `docs/project-roadmap.md` P0.4. CC #2 must use
`at.tugraz.ist.ase.hiconfit`. The README version table (just corrected to `-48` in commit
`6c37977`) also needs re-editing to `-49` as part of this bump.

---

## 7. Contract A (deliverable for CC #2)

Public surface exposed by Phase B — nothing else is added:

```java
// kb-package, at.tugraz.ist.ase.hiconfit.common
public interface SplitPointStrategy {
    int computeK(int size);
    SplitPointStrategy MIDPOINT = size -> size / 2;
}

public final class RatioSplitPointStrategy implements SplitPointStrategy {
    public RatioSplitPointStrategy(double ratio);
    public double getRatio();
    public int computeK(int size);
}

// kb-package, ConstraintUtils (@UtilityClass — call statically)
public static void split(Set<Constraint> C, Set<Constraint> C1, Set<Constraint> C2, int k);
public static void split(Set<Constraint> C, Set<Constraint> C1, Set<Constraint> C2); // midpoint

// ca-cdr-package, at.tugraz.ist.ase.hiconfit.cacdr.algorithms
public void QuickXPlain.setSplitStrategy(SplitPointStrategy s, boolean applyAtAllLevels);
public SplitPointStrategy QuickXPlain.getSplitStrategy();
public boolean QuickXPlain.isApplyAtAllLevels();
```

Dependency version for CC #2: **`at.tugraz.ist.ase.hiconfit:{kb,ca-cdr}:1.0.1-alpha-49`** (bumped).

Carry-over warnings for CC #2:
1. `RatioSplitPointStrategy(0.5)` is byte-identical to `MIDPOINT` at every size (floor), so
   brief §7.2's `0.5 / true` default is safe as written. No special-casing needed.
2. Reverse-order / r-vs-(1-r) mapping (brief §10.1) is CC #2's to verify. This side is agnostic:
   `split` always puts the **first** k constraints in C1 and QuickXPlain always recurses
   `qx(C2, C1, B ∪ C2)` first — i.e. **C1 (the first k) is the side tested for discardability
   first**. That is the fact CC #2 needs to anchor the mapping; no interpretation added here.
3. `QuickXPlainLabeler.getInstance()` now carries the strategy into clones (§2.5), so the
   HSDAG/HSTree path is safe if the experiment ever needs it. Not required for the
   `maxNumberOfConflicts == 1` path CC #2 uses.

---

## 8. Phase B execution order

1. Create the two new types (§2.1, §2.2) + `SplitPointStrategyTest` (§3.1). Run kb-package tests.
2. `ConstraintUtils` overload + delegate (§2.3). Re-run §3.1 — `splitNoArgIsUnchangedForLegacySizes`
   is the F1 gate.
3. Run the 5 untouched-caller regression suites **before** touching QuickXPlain, to prove the
   delegate is transparent.
4. `QuickXPlain` changes (§2.4) + `QuickXPlainSplitStrategyTest` (§3.2).
5. Full `mvn clean install`, agnosticism grep (§5b), then report Contract A.

Log to `plans/progress.md` as work lands.

---

## 9. Decisions (resolved 2026-07-21)

| # | Question | Decision |
|---|---|---|
| Q1 | `Ratio(0.5) != MIDPOINT` on odd sizes — `Math.round` or `Math.floor`? | **`Math.floor`** — brief revised (§5.1, §11) after planning. Divergence eliminated; equivalence pinned by test instead |
| Q2 | Version bump or reinstall in place? | **Bump to `1.0.1-alpha-49`** (13 edits, §6) |
| Q3 | Fix `QuickXPlainLabeler.getInstance()` strategy loss? | **Fix now** (§2.5) |
| Q4 | Fold a second algorithm into Phase B? | **No — QuickXPlain only.** Ship Contract A, defer §6.4 stretch goal |
| Q5 | Copyright year on new files | `2026` (not raised; assumed) |

### Still open — for CC #2 / Cowork, not blocking Phase B

1. ~~Brief §7.2's default unsafe~~ — **resolved** by the `Math.floor` revision (§5.1, §11).
2. Brief §6.4 needs correcting on the record (D3): the stretch goal is one shared seam
   (`ConstraintUtils.split`, 5 algorithms), not per-algorithm inlined halving. Materially changes
   the cost estimate for "second algorithm" if that becomes a paper requirement before 2026-07-28.
3. README version table needs re-editing `-48` → `-49` as part of the bump (§6).

Note: skipped `ck plan create` scaffolding (phase files, task hydration) — you specified
`plans/plan.md` explicitly and this is a gated two-phase flow, so the extra structure would be noise.
