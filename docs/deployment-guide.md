# Deployment Guide — HiConfiT-Core

Build, test, and publish procedures for the 11-module Maven library suite. **This is a LIBRARY set, not a deployed service** — "deployment" means releasing to Maven Central / GitHub Packages.

Current version: `1.0.1-alpha-48`

---

## Prerequisites

- **JDK 23** — exact version enforced by root pom (`<source>23</source>`)
- **Maven 3.9.x+** — system-installed, no Maven Wrapper (despite .gitignore reference to `.mvn/wrapper/maven-wrapper.jar`)
- **GitHub credentials** — for GitHub Packages auth (PAT with `read:packages`/`write:packages` scope)

**No Maven Wrapper:** The project has NO `.mvn/` directory or `mvnw` executable. Install Maven globally or use `$M2_HOME/bin/mvn`.

Verify:
```bash
java -version  # Ensure 23.x
mvn -version   # Should be 3.9+
```

---

## Build Commands

All commands executed from the repo root.

### Full build (all 11 modules)

```bash
mvn clean install
```

Runs compile → test → package → install across the reactor in dependency order.

### Single module with dependencies

```bash
mvn clean install -pl <module-name> -am
```

`-pl` = project list; `-am` = also make (build dependencies first).

Example:
```bash
mvn clean install -pl ca-cdr-package -am
```

Builds `common`, `eval`, `csp2choco`, `fm`, `kb`, `ca-cdr-core`, `cdrmodel`, then `ca-cdr`.

### Run tests

```bash
mvn test                                    # All modules
mvn test -pl <module-name>                  # Single module
mvn test -pl <module-name> -Dtest=<Class>  # Single test class
mvn test -pl <module-name> -Dtest=<Class>#<method>  # Single test method
```

Examples:
```bash
mvn test -pl ca-cdr-package
mvn test -pl ca-cdr-package -Dtest=FastDiagV2Test
mvn test -pl ca-cdr-package -Dtest=FastDiagV2Test#testDiagnosis
```

### Skip tests during build

```bash
mvn clean install -DskipTests
```

Useful for rapid iteration, but do NOT skip in release builds.

### Verify build with strict checks

```bash
mvn clean verify
```

Runs compile, test, integration-test, verify phases. Suitable pre-release.

---

## Module Reactor & Dependency Graph

The 11 modules build in this dependency order:

```
common ──┬──► csp2choco ──────────────────────┐
         ├──► fm ──┐                          │
         └──► eval ┴──► kb ──► ca-cdr-core ──┬┴──► cdrmodel ──► ca-cdr ──┬──► configurator
                                             │                           │        ▲
                                             └──► heuristics ────────────┼────────┘
                                                                         └──► fma
```

| Module | Role | Depends on |
|---|---|---|
| common | Utilities, Choco wrapper, CLI | — |
| eval | Performance counters/timers | common |
| fm | Feature model representation (5 parsers) | common |
| csp2choco | CSP text → Choco translator | common |
| kb | Knowledge base abstractions (FMKB, PCKB, CameraKB) | fm, eval |
| ca-cdr-core | TestCase, Solution, Assignment, translators | kb |
| cdrmodel | CDR problem setup, FMCdrModel | ca-cdr-core, csp2choco |
| ca-cdr | **Algorithms:** QuickXPlain, FastDiag*, FlexDiag, DirectDebug, HSDAG, WipeOutR_FM, ChocoConsistencyChecker | cdrmodel |
| heuristics | Matrix Factorization variable/value ordering | ca-cdr-core |
| configurator | Knowledge-based configurator | ca-cdr, heuristics |
| fma | Feature model anomaly analysis (6 analyses) | ca-cdr |

**Note:** The CLAUDE.md Module Architecture section incorrectly places `csp2choco` at the kb layer and says `eval` has no deps within the project. The graph above is correct.

---

## GitHub Packages Authentication

HiConfiT-Core publishes to GitHub Packages under the `HiConfiT` org.

### 1. Create a GitHub Personal Access Token (PAT)

https://github.com/settings/tokens

Scopes needed: `read:packages`, `write:packages`

### 2. Configure Maven settings.xml

Add to `~/.m2/settings.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <servers>
        <server>
            <id>github-maven-repository</id>
            <username>YOUR_GITHUB_USERNAME</username>
            <password>YOUR_PAT</password>
        </server>
    </servers>
</settings>
```

Replace `YOUR_GITHUB_USERNAME` and `YOUR_PAT` with your actual credentials.

### 3. Configure your consumer project (pom.xml)

Add the GitHub Packages repository:

```xml
<profiles>
    <profile>
        <id>github-maven-repository</id>
        <repositories>
            <repository>
                <id>github-maven-repository</id>
                <url>https://maven.pkg.github.com/HiConfiT/*</url>
            </repository>
        </repositories>
    </profile>
</profiles>
```

Activate with: `mvn <command> -Pgithub-maven-repository`

### 4. Add dependency

```xml
<dependency>
    <groupId>at.tugraz.ist.ase.hiconfit</groupId>
    <artifactId>ARTIFACT_ID</artifactId>
    <version>1.0.1-alpha-48</version>
</dependency>
```

Available artifact IDs: `common`, `eval`, `fm`, `csp2choco`, `kb`, `ca-cdr-core`, `cdrmodel`, `ca-cdr`, `heuristics`, `configurator`, `fma`.

**KNOWN ISSUE:** README.md version table says `1.0` for all 11 artifacts, but the actual current version is `1.0.1-alpha-48`. The README is stale — use the version from `pom.xml` or release tags.

---

## Release Process (Manual)

Releases are manual. There is no CI/CD, no GitHub Actions automation, and no Dependabot.

### 1. Bump version

Edit **root pom.xml** — update BOTH:

```xml
<version>1.0.1-alpha-49</version>                    <!-- Line 4 -->
<artifact.version>1.0.1-alpha-49</artifact.version>  <!-- Line 8 -->
```

These MUST be kept in sync. All 11 modules inherit `${artifact.version}` from the root.

**Recent commits show this mismatch caught and fixed:**
- 6d1012b "Bump version to 1.0.1-alpha-48"
- 22005d8 earlier version bump

### 2. Commit the version bump

```bash
git add pom.xml
git commit -m "chore: bump version to 1.0.1-alpha-49"
```

### 3. Tag the release

```bash
git tag v1.0.1-alpha-49
git push origin v1.0.1-alpha-49
```

### 4. Deploy to GitHub Packages

```bash
mvn clean verify
mvn deploy
```

**This publishes BINARY JARS ONLY** — no source JARs, no Javadoc, no signatures. The project has NO:
- `maven-gpg-plugin`
- `maven-source-plugin`
- `maven-javadoc-plugin`

Only `.jar` artifacts (and `.jar.md5` / `.jar.sha1` checksums) are uploaded to GitHub Packages.

---

## Fat-Jar Packaging

Three modules produce "fat JARs" (jar-with-dependencies) via `maven-assembly-plugin 3.3.0`:

| Module | Finalname | Usage |
|---|---|---|
| fma-package | `fma` | Standalone feature model analyzer |
| ca-cdr-package | (no assembly config) | Not actively packaged |
| configurator-package | (assembly block exists) | Standalone configurator |

For modules with assembly, the plugin runs during `package` phase. The fat JAR is *not* the default `install`ed artifact — it's a secondary output in `target/`.

To build fat JARs:
```bash
mvn clean package -pl fma-package
ls fma-package/target/fma.jar
```

---

## Testing Coverage & Hazards

**Test execution:** JUnit 5, 60+ test classes, ~350 `@Test` methods total.

Significant untested areas (see merged scout context section 4):
- `ca-cdr-core/translator/` — 8 files entirely untested (LogOp creation, requirement builders, solution readers/writers)
- `heuristics` MF pipeline — no unit tests; dead-code indicator (no in-repo callers)
- `fm/ASTBuilder.convertToCNF` — module's most intricate logic, untested
- `ChocoConsistencyChecker` — no dedicated unit test; clone-model paths untested
- `kb/RenaultKB` (1722 LOC) + `SolveErrorTest` (1620 LOC) — test files fully commented out

Run full test suite before release:
```bash
mvn clean test
```

Fix any new failures before proceeding.

---

## CI/CD Status: NONE

⚠️ **Critical risk.** The repository has NO automated testing, no release automation, no Dependabot.

**Git history shows removal:**
| Workflow | Added | Deleted |
|---|---|---|
| `.github/workflows/maven-publish.yml` | 2022-05-17 | 2023-05-25 |
| `.github/workflows/pages.yml` | 2022-09-21 | 2022-09-21 (same day) |

Tests run ONLY when a developer types `mvn test` locally — on a project that publishes to a public registry.

**Roadmap item:** Restore GitHub Actions for:
1. Run tests on every PR / push to main
2. Automated versioning (e.g., semantic-release)
3. Publish to GitHub Packages on tag

---

## Build Hazards to Know

### ANTLR Plugin Mis-Scoping (CRITICAL)

ANTLR 4 is declared as a `<dependency>` (NOT `<build><plugins>`) in:
- `csp2choco-package/pom.xml:32-43`
- `fm-package/pom.xml:44-56`

**Consequence:** `.g4` grammar files are INERT. The 5 generated Java files (1732 LOC, headers say "generated by ANTLR 4.12.0") are hand-committed into `src/main/java` and are what actually compiles.

Edits to `.g4` files have ZERO build effect. To regenerate, run ANTLR manually outside the build.

Three ANTLR versions coexist:
- `4.12.0` — csp2choco generated files (stale headers reference obsolete absolute path)
- `4.13.1` — fm-package `pom.xml` declares it (unused)
- `4.13.2` — fm4conf generated files (newest)

**Fix (future):** Move `antlr4-maven-plugin` to `<build><plugins>`, not `<dependencies>`.

### Surefire Version (2018-era)

`maven-surefire-plugin 2.22.0` (2018) is pinned in the root pom. Current is 3.5.x. The old version works but misses modern features and bug fixes. Consider an upgrade, but test thoroughly.

### No pluginManagement / dependencyManagement

Versions are duplicated per module. `maven-assembly-plugin 3.3.0` appears in 3 module poms; `antlr4-maven-plugin` in 2. No central version control means consistency is manual.

### Mahout 0.9 / 0.13.0 Version Mismatch

`heuristics-package/pom.xml` declares:
```xml
<mahout-core>0.9</mahout-core>
<mahout-math>0.13.0</mahout-math>
```

Incompatible versions with a comment: "there is a conflict if upgrade this". Mahout 0.9 is from 2014 — potential CVE/maintenance liability. Investigate whether this is necessary; if not, upgrade to current Mahout or remove the dependency.

---

## Working Tree Artifacts

Ignored by `.gitignore` but present in the working tree:

- `ca-cdr-package/.DS_Store` (tracked-adjacent; macOS metadata)
- `heuristics-package/logs.log` (untracked; runtime logs)

These can safely be deleted locally but do not affect builds.

---

## Troubleshooting

### `Cannot find symbol` after `.g4` edit

Your `.g4` changes had no effect. Run ANTLR manually or regenerate via your IDE plugin.

### `java.lang.NullPointerException` in HSTree constructor

Forgot to call `setPruningEngine(new HSDAGPruningEngine(...))` after creating HSDAG. This is required; NPE is the signal.

### `ClassCastException` in HSDAG.expand()

Passed an `HSTreePruningEngine` to HSDAG (which expects `HSDAGPruningEngine`). HSDAG downcasts unsafely.

### Stale test classes in kb/

`RenaultKBTest.java` and `SolveErrorTest.java` are fully commented out and use pre-rename package names (`at.tugraz.ist.ase.kb.*`). They will not compile if uncommented. Either delete them or migrate to modern namespace and assertion style.

### Performance evaluation counters not resetting

`PerformanceEvaluator` holds static global state. Call `PerformanceEvaluator.reset()` between test runs, or you'll see cumulative metrics from all prior tests.

---

## Release Checklist

- [ ] Verify no uncommitted changes
- [ ] Run `mvn clean verify` — all tests pass
- [ ] Update version in root `pom.xml` (both `<version>` and `<artifact.version>`)
- [ ] Commit version bump with message "chore: bump version to X.Y.Z"
- [ ] Tag commit: `git tag vX.Y.Z`
- [ ] Push tag to origin
- [ ] Run `mvn clean deploy` (requires GitHub Packages credentials in settings.xml)
- [ ] Verify artifacts appear in GitHub Packages UI
- [ ] Update README.md version table (currently stale — says 1.0 for all)

---

## Links

- **GitHub Packages:** https://github.com/HiConfiT/hiconfit-core/packages
- **PAT creation:** https://github.com/settings/tokens
- **Documentation:** https://hiconfit.github.io (canonical)
- **See also:** `docs/system-architecture.md` for corrected dependency graph diagrams
