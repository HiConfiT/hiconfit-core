# CA-CDR
A Maven package for Consistency-based Algorithms for Conflict Detection and Resolution (CA-CDR).

Conflict Detection and Resolution is a substantial task in Knowledge Base Engineering (KBE). Intelligent mechanisms are urgently needed, especially in large-scale knowledge bases. This repository publishes our implementations for some/our consistency-based algorithms, which can be utilized in all three phases of KBE, i.e., design, testing and debugging, and configuration.

*If you use my implementations in your research, please cite the papers listed in the References.*

## List of algorithms:
1. [QuickXPlain](https://github.com/manleviet/CA-CDR/blob/main/src/main/java/at/tugraz/ist/ase/cacdr/algorithms/QuickXPlain.java) [1]
2. [FastDiag](https://github.com/manleviet/CA-CDR/blob/main/src/main/java/at/tugraz/ist/ase/cacdr/algorithms/FastDiagV2.java) [2]
3. [MSS-based FastDiag](https://github.com/manleviet/CA-CDR/blob/main/src/main/java/at/tugraz/ist/ase/cacdr/algorithms/FastDiagV3.java)
4. [FlexDiag](https://github.com/manleviet/CA-CDR/blob/main/src/main/java/at/tugraz/ist/ase/cacdr/algorithms/FlexDiag.java) [3]
5. [HS-tree](https://github.com/manleviet/CA-CDR/blob/main/src/main/java/at/tugraz/ist/ase/cacdr/algorithms/hsdag/HSTree.java) [8]
6. [HSDAG](https://github.com/manleviet/CA-CDR/blob/main/src/main/java/at/tugraz/ist/ase/cacdr/algorithms/hsdag/HSDAG.java) [9]
7. [DirectDebug]() [4, 5, 6, 7]
8. (coming soon) KBDiag (the related paper submitted in January 2022)
9. (coming soon) WipeOutR_T (the related paper submitted in October 2021)
10. (coming soon) WipeOutR_FM (the related paper submitted in October 2021)
11. (coming soon) AggregatedTest (the related paper submitted in November 2021)
12. (coming soon) InformedQX
13. (coming soon) FastDiagP

## Examples

There are some test models in [here](https://github.com/manleviet/CDRModel/tree/main/src/main/java/at/tugraz/ist/ase/cdrmodel/test/model) and some examples, showing how to use these algorithms, in [here](https://github.com/manleviet/CA-CDR/tree/main/src/test/java/at/tugraz/ist/ase/cacdr/algorithms).

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
