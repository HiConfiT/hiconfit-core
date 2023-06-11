# hiconfit-core

A set of Maven-based libraries for High-Performance Knowledge Based Configuration Techniques.

Documentation: https://hiconfit.manleviet.info

*If you use our implementations in your research, please cite the papers listed in the [References](#references).*

## Table of Contents

- [Key libraries](#key-libraries)
  - [ca-cdr](#ca-cdr)
  - [fma](#fma)
  - [configurator](#configurator)
- [All libraries of hiconfit-core](#all-libraries-of-hiconfit-core)
- [How to get the libraries](#how-to-get-the-libraries)
- [References](#references)

## Key libraries

### ca-cdr

_**ca-cdr**_ is a library of Consistency-based Algorithms for Conflict Detection and Resolution.

_Conflict Detection and Resolution_ (CDR) is a substantial task in _Knowledge Base Engineering_ (KBE). 
_**ca-cdr**_ publishes our implementations of CDR consistency-based algorithms, 
which can be utilized in all three phases of KBE, i.e., _design_, _testing and debugging_, and _configuration_.

##### List of algorithms:

1. [QuickXPlain](quickxplain) [1]
2. [FastDiag](fastdiag) [2]
3. [MSS-based FastDiag](mss_fastdiag) [15]
4. [FlexDiag](flexdiag) [3]
5. [HS-tree](hstree) [8]
6. [HSDAG](hsdag) [9]
7. [DirectDebug](directdebug) [4, 5, 6, 7]
8. [DirectDiag](directdiag)
9. [WipeOutR_T](wipeoutr_t) [12, 13]
10. [WipeOutR_FM](wipeoutr_fm) [12, 13]
11. (coming soon) AggregatedTest [14]
12. (coming soon) LevelWiseParallelHSDAG [10, 11]
13. (coming soon) FullParallelHSDAG [10, 11]
14. (coming soon) FastDiagP [15] - [Python implementation]
15. (coming soon) KBDiag [the related paper submitted on April 2023]
16. (coming soon) InformedQX
17. (coming soon) ParallelWipeOutR_T
18. (coming soon) ParallelWipeOutR_FM

### fma

_**fma**_ is a library for Feature Model Testing and Debugging.

Two key features:

1. _**fma**_ provides a mechanism to automatically generate property-based test cases for feature models. Test cases 
are generated based on six basic analysis operations, including _void feature models_, _dead features_, 
_conditionally dead features_, _full mandatory features_, _false optional features_, and _redundant constraints_, 
which are considered as anomalies in feature models.
2. On the basis of the generated test cases, _**fma**_ allows to check whether a feature model contains one 
or more of these six anomalies and execute asynchronously test case validation as well as corresponding diagnosis.
On the basis of three algorithms, i.e., _**DirectDebug**_, _**WipeOutR_FM**_, and _**HSDAG**_, _**fma**_ determines all
explanations for the anomalies in the feature model.

### configurator

_**configurator**_ offers a compact knowledge-based configurator 
that supports the state-of-the-art Matrix Factorization-based Configuration and Recommendation.

## All libraries of hiconfit-core

**hiconfit-core** is organized in 11 following Maven libraries:

| *library*      | *description*                                                                                                                                                                                              |
|:---------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [common]       | provides utility functions                                                                                                                                                                                 |
| [csp2choco]    | provides a translator converting CSP constraints into Choco Solver commands                                                                                                                                |
| [eval]         | provides a performance evaluator, i.e., counters and timers, which could be used to measure the performance of algorithms                                                                                  |
| [fm]           | provides the management functionalities for basic feature models                                                                                                                                           |
| [kb]           | provides classes managing CSP (Choco) representations of a knowlege base/feature model                                                                                                                     |
| [ca-cdr-core]  | provides core classes for representing user requirements and solutions of a configurator as well as for managing test cases and test suites                                                                |
| [cdrmodel]     | provides an programmatic approach to manage/prepare the constraints/test cases for consistency-based algorithms                                                                                            |
| [ca-cdr]       | provides implementations of Consistency-based Algorithms for Conflict Detection and Resolution (CA-CDR) and a ChocoConsistencyChecker                                                                      |
| [heuristics]   | provides an implementation of Matrix Factorization Based Variable and Value Ordering Heuristics for Constraint Solving and a wrapper for Matrix Factorization algorithm on the basis of the Mahout library |
| [configurator] | provides a compact knowledge-based configurator supporting Matrix Factorization based Configuration and Recommendation                                                                                     |
| [fma]          | provides a mechanism to automatically generate property-based test cases for feature models and allows the automated determination of faulty constraints in the feature model                               |

## How to get the libraries

### Authenticating to GitHub Packages

In your Maven project, please add the below script in the `settings.xml` file.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <servers>
        <server>
            <id>github-maven-repository</id>
            <username>USERNAME</username>
            <password>TOKEN</password>
        </server>
    </servers>
</settings>
```

Replacing `USERNAME` with your GitHub username, and `TOKEN` with your personal access token (see [Creating a personal access token]).

### Connecting to our repositories

Add also the below script into the `pom.xml` file:

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

### Installing the libraries

Add the library dependencies to the `dependencies` element of your project `pom.xml` file.

```xml
<dependency>
    <groupId>at.tugraz.ist.ase</groupId>
    <artifactId>ARTIFACT_ID</artifactId>
    <version>VERSION</version>
</dependency>
```

Replacing `ARTIFACT_ID` and `VERSION` with the corresponding information from the following table.
_Legend_: <span>stable version</span>{: .label .label-green .fs-1 } <span>latest version</span>{: .label .label-purple .fs-1 }

| *artifact_id*  | *versions* | *description*                                                                                                                                                                                              |
|:---------------|:-----------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [common]       | 1.0        | provides utility functions                                                                                                                                                                                 |
| [csp2choco]    | 1.0        | provides a translator converting CSP constraints into Choco Solver commands                                                                                                                                |
| [eval]         | 1.0        | provides a performance evaluator, i.e., counters and timers, which could be used to measure the performance of algorithms                                                                                  |
| [fm]           | 1.0        | provides the management functionalities for basic feature models                                                                                                                                           |
| [kb]           | 1.0        | provides classes managing CSP (Choco) representations of a knowlege base/feature model                                                                                                                     |
| [ca-cdr-core]  | 1.0        | provides core classes for representing user requirements and solutions of a configurator as well as for managing test cases and test suites                                                                |
| [cdrmodel]     | 1.0        | provides an programmatic approach to manage/prepare the constraints/test cases for consistency-based algorithms                                                                                            |
| [ca-cdr]       | 1.0        | provides implementations of Consistency-based Algorithms for Conflict Detection and Resolution (CA-CDR) and a ChocoConsistencyChecker                                                                      |
| [heuristics]   | 1.0        | provides an implementation of Matrix Factorization Based Variable and Value Ordering Heuristics for Constraint Solving and a wrapper for Matrix Factorization algorithm on the basis of the Mahout library |
| [configurator] | 1.0        | provides a compact knolwedge-based configurator supporting Matrix Factorization based Configuration and Recommendation                                                                                     |
| [fma]          | 1.0        | provides a mechnism to automatically generate property-based test cases for feature models and allows the automated determination of faulty constraints in the feature model                               |

## References
1. U. Junker. 2004. QuickXPlain: preferred explanations and relaxations for over-constrained problems. *In Proceedings of the 19th national conference on Artificial intelligence (AAAI'04)*. AAAI Press, 167–172. [[ACM](https://dl.acm.org/doi/abs/10.5555/1597148.1597177)]
2. A. Felfernig, M. Schubert, and C. Zehentner. 2012. An efficient diagnosis algorithm for inconsistent constraint sets. *Artif. Intell. Eng. Des. Anal. Manuf.* 26, 1 (February 2012), 53–62. [[Cambridge Core](https://doi.org/10.1017/S0890060411000011)]
3. A. Felfernig, R. Walter, J.A. Galindo, et al. 2018. Anytime diagnosis for reconfiguration. *J Intell Inf Syst* 51, 161–182 (2018). [[Springer](https://doi.org/10.1007/s10844-017-0492-1)]
4. V.M. Le, A. Felfernig, M. Uta, D. Benavides, J. Galindo, and T.N.T. Tran. 2021. DirectDebug: Automated Testing and Debugging of Feature Models, *2021 IEEE/ACM 43rd International Conference on Software Engineering: New Ideas and Emerging Results (ICSE-NIER)*, 2021, pp. 81-85. [[IEEE](https://doi.org/10.1109/ICSE-NIER52604.2021.00025)]
5. V.M. Le, A. Felfernig, T.N.T. Tran, M. Atas, M. Uta, D. Benavides, J. Galindo. 2021. DirectDebug: A software package for the automated testing and debugging of feature models, *Software Impacts*, Volume 9, 2021, 100085, ISSN 2665-9638. [[Elsevier](https://doi.org/10.1016/j.simpa.2021.100085)]
6. DirectDebug's Original version with an evaluation in [GitHub](https://github.com/AIG-ist-tugraz/DirectDebug).
7. An executable evaluation of DirectDebug in [CodeOcean](https://codeocean.com/capsule/5824065/tree/v1).
8. R. Reiter. 1987. A theory of diagnosis from first principles, *Artificial Intelligence*, Volume 32, Issue 1, 1987, pp. 57-95, ISSN 0004-3702. [[ScienceDirect](https://doi.org/10.1016/0004-3702(87)90062-2)]
9. R. Greiner, B. A. Smith, and R. W. Wilkerson. 1989. A correction to the algorithm in reiter’s theory of diagnosis, *Artif Intell*, vol. 41, no. 1, pp. 79–88. [[ScienceDirect](https://doi.org/10.1016/0004-3702(89)90079-9)]
10. D. Jannach, T. Schmitz, and K. Shchekotykhin. 2016. Parallel model-based diagnosis on multi-core computers. *Journal of Artificial Intelligence Research* 55 (2016): 835-887. [[JAIR](https://doi.org/10.1613/jair.5001)]
11. D. Jannach, T. Schmitz, and K. Shchekotykhin. 2015. Parallelized Hitting Set Computation for Model-Based Diagnosis. *Proceedings of the AAAI Conference on Artificial Intelligence*, 29(1). [[AAAI](https://doi.org/10.1609/aaai.v29i1.9389)]
12. V.M. Le, A. Felfernig, M. Uta, T.N.T. Tran, and C. Vidal. 2022. WipeOutR: Automated Redundancy Detection for Feature Models, *26th ACM International Systems and Software Product Line Conference* (SPLC 2022). [[ACM](https://doi.org/10.1145/3546932.3546992)]
13. An evaluation of WipeOutR algorithms in [GitHub](https://github.com/AIG-ist-tugraz/WipeOutR).
14. V.M. Le, A. Felfernig, and T.N.T. Tran. 2022. Test Case Aggregation for Efficient Feature Model Testing, *26th ACM International Systems and Software Product Line Conference* (SPLC 2022) - Volume B. [[ACM](https://doi.org/10.1145/3503229.3547046)]
15. V.M. Le, C.V. Silva, A. Felfernig, T.N.T. Tran, J. Galindo, D. Benavides. 2023. FastDiagP: An Algorithm for Parallelized Direct Diagnosis. *In 37th AAAI Conference on Artificial Intelligence*. AAAI’23, Washington, DC, USA. [[arXiv](https://arxiv.org/pdf/2305.06951.pdf)]
16. An evaluation of FastDiagP algorithm in [GitHub](https://github.com/AIG-ist-tugraz/FastDiagP).

[//]: # (links)
