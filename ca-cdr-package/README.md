# ca-cdr-package

A Maven package for Consistency-based Algorithms for Conflict Detection and Resolution (CA-CDR).

For further details about the library, please refer to the [Documentation].

## Features:

- algorithms:

  1. [QuickXPlain] [1]
  2. [FastDiag] [2]
  3. [MSS-based FastDiag] [15]
  4. [FlexDiag] [3]
  5. [HS-tree] [8]
  6. [HSDAG] [9]
  7. [DirectDebug] [4, 5, 6, 7]
  8. [DirectDiag]
  9. WipeOutR_T [12, 13]
  10. [WipeOutR_FM] [12, 13]
  11. (coming soon) AggregatedTest [14]
  12. (coming soon) LevelWiseParallelHSDAG [10, 11]
  13. (coming soon) FullParallelHSDAG [10, 11]
  14. (coming soon) FastDiagP [15] - [Python implementation]
  15. (coming soon) KBDiag [the related paper submitted on April 2023]
  16. (coming soon) InformedQX
  17. (coming soon) ParallelWipeOutR_T
  18. (coming soon) ParallelWipeOutR_FM

- a [Choco Consistency Checker], supporting the consistency checks for sets of constraints or sets of test cases.

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

[Documentation]: https://hiconfit.manleviet.info
[QuickXPlain]: https://github.com/HiConfiT/hiconfit-core/blob/main/ca-cdr-package/src/main/java/at/tugraz/ist/ase/hiconfit/cacdr/algorithms/QuickXPlain.java
[FastDiag]: https://github.com/HiConfiT/hiconfit-core/blob/main/ca-cdr-package/src/main/java/at/tugraz/ist/ase/hiconfit/cacdr/algorithms/FastDiagV2.java
[MSS-based FastDiag]: https://github.com/HiConfiT/hiconfit-core/blob/main/ca-cdr-package/src/main/java/at/tugraz/ist/ase/hiconfit/cacdr/algorithms/FastDiagV3.java
[FlexDiag]: https://github.com/HiConfiT/hiconfit-core/blob/main/ca-cdr-package/src/main/java/at/tugraz/ist/ase/hiconfit/cacdr/algorithms/FlexDiag.java
[HS-tree]: https://github.com/HiConfiT/hiconfit-core/blob/main/ca-cdr-package/src/main/java/at/tugraz/ist/ase/hiconfit/cacdr/algorithms/hs/HSTree.java
[HSDAG]: https://github.com/HiConfiT/hiconfit-core/blob/main/ca-cdr-package/src/main/java/at/tugraz/ist/ase/hiconfit/cacdr/algorithms/hs/HSDAG.java
[DirectDebug]: https://github.com/HiConfiT/hiconfit-core/blob/main/ca-cdr-package/src/main/java/at/tugraz/ist/ase/hiconfit/cacdr/algorithms/DirectDebug.java
[DirectDiag]: https://github.com/HiConfiT/hiconfit-core/blob/main/ca-cdr-package/src/main/java/at/tugraz/ist/ase/hiconfit/cacdr/algorithms/DirectDiag.java
[WipeOutR_FM]: https://github.com/HiConfiT/hiconfit-core/blob/main/ca-cdr-package/src/main/java/at/tugraz/ist/ase/hiconfit/cacdr/algorithms/WipeOutR_FM.java
[Python implementation]: https://github.com/AIG-ist-tugraz/FastDiagP
[Choco Consistency Checker]: https://github.com/HiConfiT/hiconfit-core/blob/main/ca-cdr-package/src/main/java/at/tugraz/ist/ase/hiconfit/cacdr/checker/ChocoConsistencyChecker.java
