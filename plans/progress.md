# Progress — Injectable split point for QuickXPlain (CC #1)

Plan: [`plan.md`](./plan.md) · Design brief (WHAT/WHY): `ML4QX/design-learned-split-ratio.md`
Repo: `hiconfit-core` only · Branch: `dev`

---

## 2026-07-21 — Phase A (plan) — DONE

Read brief + actual source. Wrote `plan.md` with per-file diffs verified against real code.
Two findings changed the brief's proposed code (see `plan.md` §0, §5).

## 2026-07-21 — Phase B (implement) — DONE

Executed in `plan.md` §8 order. All gates green.

| Step | Action | Result |
|---|---|---|
| 1 | `SplitPointStrategy` + `RatioSplitPointStrategy` created (kb-package `common`) | — |
| 2 | `SplitPointStrategyTest` | **7/7 green** |
| 3 | `ConstraintUtils` k-aware overload + midpoint delegate | — |
| 4 | Regression: 5 untouched split callers, **before** touching QuickXPlain | **31/31 green** — delegate proven transparent |
| 5 | `QuickXPlain` fields/setter/accessors + strategy threaded through `qx` | — |
| 6 | `QuickXPlainLabeler.getInstance` carries strategy into clones | — |
| 7 | `QuickXPlainSplitStrategyTest` | **9/9 green** |
| 8 | Version bump `1.0.1-alpha-48` → `-49` (13 pom edits + README table + docs) | zero stale refs |
| 9 | Full reactor `mvn clean install` | **BUILD SUCCESS**, all 12 modules, ca-cdr 116 tests |
| 10 | Agnosticism grep | **zero matches** |
| 11 | Reordering grep | **zero** sort/reverse/comparator in new or changed algorithm code |

Installed to local `~/.m2`:
`at.tugraz.ist.ase.hiconfit:kb:1.0.1-alpha-49`, `at.tugraz.ist.ase.hiconfit:ca-cdr:1.0.1-alpha-49`
(verified `SplitPointStrategy.class` + `RatioSplitPointStrategy.class` present in the kb jar).

### Decisions applied

| # | Decision | Source |
|---|---|---|
| Q1 | `Math.floor`, not `Math.round` | brief revised §5.1/§11 mid-implementation; makes `Ratio(0.5)` byte-identical to `MIDPOINT` at every size |
| Q2 | Bump to `1.0.1-alpha-49` | user |
| Q3 | Fix labeler clone strategy loss now | user |
| Q4 | QuickXPlain only; defer second algorithm | user |
| Q5 | Copyright `2026` on new files | assumed |

### Deviation from the brief, and why

`ConstraintUtils.split(…, k)` clamps **conditionally**, not unconditionally as brief §6.2 shows:

```java
k = (n >= 2) ? Math.max(1, Math.min(k, n - 1)) : Math.max(0, Math.min(k, n));
```

The brief's unconditional `max(1, min(k, n-1))` regresses the no-arg overload below n=2 —
`n==0` throws `IndexOutOfBoundsException`, `n==1` swaps the halves. All 6 in-repo callers guard
`n>=2` so nothing breaks today, but `split` is published public API. Pinned by
`splitNoArgIsUnchangedForLegacySizes`.

### Test notes

`applyAtAllLevelsControlsHowDeepTheStrategyReaches` uses a call-counting strategy rather than
asserting counter deltas on specific models: it proves the strategy actually reaches the recursion
(`applyAtAllLevels=false` ⇒ consulted exactly once) independent of any model's shape. Without it,
a strategy silently ignored inside `qx` would still pass every correctness test.

---

## Handoff

**Contract A is live** — see `plan.md` §7 for the exact surface and the CC #2 carry-over notes.

Open, not blocking CC #2:
1. Brief §6.4 needs correcting on the record — `FastDiagV2/V3`, `FlexDiag`, `DirectDiag`,
   `DirectDebug` all already share `ConstraintUtils.split`; only the `@Deprecated` `FastDiag.java`
   inlines its own halving. The "second algorithm" stretch goal is ~3 lines each, not a rewrite.
2. Nothing committed yet — working tree holds Phase B.
