# CA-CDR

Maven packages for Consistency-based Algorithms for Conflict Detection and Resolution (CA-CDR).

Conflict Detection and Resolution is a substantial task in Knowledge Base Engineering (KBE). Intelligent mechanisms are urgently needed, especially in large-scale knowledge bases. This repository publishes our implementations for some/our consistency-based algorithms, which can be utilized in all three phases of KBE, i.e., design, testing and debugging, and configuration.

*If you use my implementations in your research, please cite the papers listed in the References.*

## Table of Contents

- [List of algorithms](#list-of-algorithms)
- [What the CA-CDR library provides](#what-the-ca-cdr-library-provides)
- [How to get the CA-CDR packages](#how-to-get-the-ca-cdr-packages)
- [References](#references)

## List of algorithms:
1. [QuickXPlain](https://github.com/manleviet/CA-CDR-V2/blob/main/ca-cdr-package/src/main/java/at/tugraz/ist/ase/cacdr/algorithms/QuickXPlain.java) [1]
2. [FastDiag](https://github.com/manleviet/CA-CDR-V2/blob/main/ca-cdr-package/src/main/java/at/tugraz/ist/ase/cacdr/algorithms/FastDiagV2.java) [2]
3. [MSS-based FastDiag](https://github.com/manleviet/CA-CDR-V2/blob/main/ca-cdr-package/src/main/java/at/tugraz/ist/ase/cacdr/algorithms/FastDiagV3.java)
4. [FlexDiag](https://github.com/manleviet/CA-CDR-V2/blob/main/ca-cdr-package/src/main/java/at/tugraz/ist/ase/cacdr/algorithms/FlexDiag.java) [3]
5. [HS-tree](https://github.com/manleviet/CA-CDR-V2/blob/main/ca-cdr-package/src/main/java/at/tugraz/ist/ase/cacdr/algorithms/hs/HSTree.java) [8]
6. [HSDAG](https://github.com/manleviet/CA-CDR-V2/blob/main/ca-cdr-package/src/main/java/at/tugraz/ist/ase/cacdr/algorithms/hs/HSDAG.java) [9]
7. [DirectDebug](https://github.com/manleviet/CA-CDR-V2/blob/main/ca-cdr-package/src/main/java/at/tugraz/ist/ase/cacdr/algorithms/DirectDebug.java) [4, 5, 6, 7]
8. (coming soon) KBDiag
9. (coming soon) WipeOutR_T [12]
10. (coming soon) WipeOutR_FM [12]
11. (coming soon) AggregatedTest [13]
12. (coming soon) InformedQX
13. (coming soon) LevelWiseParallelHSDAG [10, 11]
14. (coming soon) FullParallelHSDAG [10, 11]
15. (coming soon) FastDiagP

### Examples

A good example in [here](https://github.com/manleviet/FlexDiagTest).

Besides, there are some test models in [here](https://github.com/manleviet/CDRModel/tree/main/src/main/java/at/tugraz/ist/ase/cdrmodel/test/model) and some examples showing how to use these algorithms in [here](https://github.com/manleviet/CA-CDR/tree/main/src/test/java/at/tugraz/ist/ase/cacdr/algorithms).

## What the CA-CDR library provides

In this repository, I published eight Maven packages and two apps as the followings:

| *Maven packages*                                       | *description*                            |
|----------------------------------------------|------------------------------------------|
| [at.tugraz.ist.ase.ca-cdr-v2](https://github.com/manleviet/CA-CDR-V2/packages/1417091)     | provides implementations of Consistency-based Algorithms for Conflict Detection and Resolution (CA-CDR) and a ChocoConsistencyChecker |
| [at.tugraz.ist.ase.cdrmodel-v2](https://github.com/manleviet/CA-CDR-V2/packages/1408661) | provides an programmatic approach to manage/prepare the constraints/test cases for consistency-based algorithms |
| [at.tugraz.ist.ase.choco-kb-v2](https://github.com/manleviet/CA-CDR-V2/packages/1408660)    | provides classes managing CSP (Choco) representations of a knowlege base/feature model |
| [at.tugraz.ist.ase.fm-v2](https://github.com/manleviet/CA-CDR-V2/packages/1408657)         | provides the management functionalities for basic feature models |
| [at.tugraz.ist.ase.eval-v2](https://github.com/manleviet/CA-CDR-V2/packages/1408656)      | provides a performance evaluator, i.e., counters and timers, which could be used to measure the performance of algorithms |
| [at.tugraz.ist.ase.test-v2](https://github.com/manleviet/CA-CDR-V2/packages/1408658)         | provides core functionalities related to knolwedge base testing and debugging tasks |
| [at.tugraz.ist.ase.csp2choco-v2](https://github.com/manleviet/CA-CDR-V2/packages/1408654) | provides a translator which enables converting CSP constraints into Choco Solver commands |
| [at.tugraz.ist.ase.common-v2](https://github.com/manleviet/CA-CDR-V2/packages/1408257) | a Maven package for utility functions |

| *apps*                                       | *description*                            |
|----------------------------------------------|------------------------------------------|
| [kbstatistics](https://github.com/manleviet/CA-CDR-V2/releases/tag/kbstatistics-v1.3.1) | calculate statistics of given knowledge bases |
| [fm-gen](https://github.com/manleviet/CA-CDR-V2/releases/tag/fm-gen-v1.3) | generates synthesized feature models using the [Betty framework](https://www.isa.us.es/betty/welcome) |

## How to get the CA-CDR packages

Please add the script below in the *settings.xml* file to download the Maven dependencies from the GitHub repository.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <activeProfiles>
        <activeProfile>github</activeProfile>
    </activeProfiles>

    <profiles>
        <profile>
            <id>github</id>
            <repositories>
                <repository>
                    <id>central</id>
                    <url>https://repo1.maven.org/maven2</url>
                </repository>
                <repository>
                    <id>github</id>
                    <url>https://maven.pkg.github.com/manleviet/*</url>
                </repository>
            </repositories>
        </profile>
    </profiles>
    
    <servers>
        <server>
            <id>github</id>
            <username>USERNAME</username>
            <password>TOKEN</password>
        </server>
    </servers>
</settings>
```
Replacing USERNAME with your GitHub username, and TOKEN with your personal access token 
(see [Creating a personal access token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)). Note: your token must have the ```read:packages``` scope.

## References
1. U. Junker. 2004. QuickXPlain: preferred explanations and relaxations for over-constrained problems. In Proceedings of the 19th national conference on Artificial intelligence (AAAI'04). AAAI Press, 167–172. https://dl.acm.org/doi/abs/10.5555/1597148.1597177
2. A. Felfernig, M. Schubert, and C. Zehentner. 2012. An efficient diagnosis algorithm for inconsistent constraint sets. Artif. Intell. Eng. Des. Anal. Manuf. 26, 1 (February 2012), 53–62. DOI:https://doi.org/10.1017/S0890060411000011
3. Felfernig, A., Walter, R., Galindo, J.A. et al. Anytime diagnosis for reconfiguration. J Intell Inf Syst 51, 161–182 (2018). https://doi.org/10.1007/s10844-017-0492-1
4. V.M. Le, A. Felfernig, M. Uta, D. Benavides, J. Galindo, and T.N.T. Tran, DIRECTDEBUG: Automated Testing and Debugging of Feature Models, 2021 IEEE/ACM 43rd International Conference on Software Engineering: New Ideas and Emerging Results (ICSE-NIER), 2021, pp. 81-85, doi: https://doi.org/10.1109/ICSE-NIER52604.2021.00025.
5. V.M. Le, A. Felfernig, T.N.T. Tran, M. Atas, M. Uta, D. Benavides, J. Galindo, DirectDebug: A software package for the automated testing and debugging of feature models, Software Impacts, Volume 9, 2021, 100085, ISSN 2665-9638, https://doi.org/10.1016/j.simpa.2021.100085.
6. DirectDebug's Original version with an evaluation in [https://github.com/AIG-ist-tugraz/DirectDebug](https://github.com/AIG-ist-tugraz/DirectDebug).
7. An executable evaluation of DirectDebug on CodeOcean [https://codeocean.com/capsule/5824065/tree/v1](https://codeocean.com/capsule/5824065/tree/v1)
8. R. Reiter, A theory of diagnosis from first principles, Artificial Intelligence, Volume 32, Issue 1, 1987, pp. 57-95, ISSN 0004-3702, https://doi.org/10.1016/0004-3702(87)90062-2.
9. R. Greiner, B. A. Smith, and R. W. Wilkerson, A correction to the algorithm in reiter’s theory of diagnosis, Artif Intell, vol. 41, no. 1, pp. 79–88, 1989, https://doi.org/10.1016/0004-3702(89)90079-9.
10. Jannach, Dietmar, Thomas Schmitz, and Kostyantyn Shchekotykhin. "Parallel model-based diagnosis on multi-core computers." Journal of Artificial Intelligence Research 55 (2016): 835-887. https://doi.org/10.1613/jair.5001.
11. Jannach, D., Schmitz, T., & Shchekotykhin, K. (2015). Parallelized Hitting Set Computation for Model-Based Diagnosis. Proceedings of the AAAI Conference on Artificial Intelligence, 29(1). https://doi.org/10.1609/aaai.v29i1.9389.
12. V.M. Le, A. Felfernig, M. Uta, T.N.T. Tran, and C. Vidal, WipeOutR: Automated Redundancy Detection for Feature Models, 26th ACM International Systems and Software Product Line Conference (SPLC 2022) - Volume A, 2022.
13. V.M. Le, A. Felfernig, and T.N.T. Tran, Test Case Aggregation for Efficient Feature Model Testing, 26th ACM International Systems and Software Product Line Conference (SPLC 2022) - Volume B, 2022.
