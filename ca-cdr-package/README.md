# ca-cdr-package

A Maven package for Consistency-based Algorithms for Conflict Detection and Resolution (CA-CDR).

For further details about the library, please refer to the [Documentation](https://hiconfit.manleviet.info).

## List of algorithms:

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

This package also provides a [Choco Consistency Checker](https://github.com/manleviet/CA-CDR-V2/blob/main/ca-cdr-package/src/main/java/at/tugraz/ist/ase/cacdr/checker/ChocoConsistencyChecker.java), supporting the consistency checks for sets of constraints or sets of test cases.

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
13. An evaluation of WipeOutR algorithms in https://github.com/AIG-ist-tugraz/WipeOutR.
14. V.M. Le, A. Felfernig, and T.N.T. Tran, Test Case Aggregation for Efficient Feature Model Testing, 26th ACM International Systems and Software Product Line Conference (SPLC 2022) - Volume B, 2022. https://doi.org/10.1145/3503229.3547046
15. V.M. Le, C.V. Silva, A. Felfernig, T.N.T. Tran, J. Galindo, D. Benavides. FastDiagP: An Algorithm for Parallelized Direct Diagnosis. In 37th AAAI Conference on Artificial Intelligence. AAAI’23, Washington, DC, USA. 2023. (to appear)
