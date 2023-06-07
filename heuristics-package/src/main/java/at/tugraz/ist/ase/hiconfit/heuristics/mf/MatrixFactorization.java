/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2021-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.heuristics.mf;

import at.tugraz.ist.ase.hiconfit.common.LoggerUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.recommender.svd.Factorization;
import org.apache.mahout.cf.taste.impl.recommender.svd.Factorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDPlusPlusFactorizer;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.math.DenseMatrix;
import org.apache.mahout.math.Matrix;

import static at.tugraz.ist.ase.hiconfit.eval.PerformanceEvaluator.start;
import static at.tugraz.ist.ase.hiconfit.eval.PerformanceEvaluator.stop;

/**
 * Represents an SVD-based Matrix Factorization
 *
 * @version 1.0
 * @author Seda Polat Erdeniz (AIG, TUGraz)
 * Source: <a href="https://github.com/CSPHeuristix/CSPHeuristix">...</a>
 *
 * @version 2.0 - 08/2021
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Slf4j
public class MatrixFactorization {
    // Factorizers:
    // 1. Alternating Least Squares (ALSWRFactorizer)
    // 2. SVD++ (SVDPlusPlusFactorizer)
    // 3. Stochastic Gradient Descent (ParallelSGDFactorizer)

    // for evaluation
    public static final String TIMER_SVD = "Timer for SVD";

    private final DataModel dataModel;

    // TODO: remove IF, UF, IFMatrix, UFMatrix, X_Prime
    @Getter // remove after test
    private double[][] IF; // item feature matrix

    @Getter // remove after test
    private double[][] UF; // user feature matrix

    @Getter
    private Matrix IFMatrix = null;

    @Getter
    private Matrix UFMatrix = null;

    @Getter
    private Matrix X_Prime = null;

    ///////////// Default Settings /////////////
    int minNumFeatures = 6;  // mxk, kxn -> k value
    int minNumIterations = 5;
    ////////////////////////////////////////////

    public MatrixFactorization(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    public int getNumUsers() {
        if (UFMatrix != null) {
            return UFMatrix.rowSize();
        }
        return 0;
    }

    public int getNumItems() {
        if (IFMatrix != null) {
            return IFMatrix.rowSize();
        }
        return 0;
    }

    public void svd(int numFeatures, int numIterations) throws TasteException {
        log.debug("{}Factorizing...", LoggerUtils.tab());
        numFeatures = Math.max(numFeatures, minNumFeatures);
        numIterations = Math.max(numIterations, minNumIterations);

        start(TIMER_SVD);

        Factorizer svdFactorizer = new SVDPlusPlusFactorizer(dataModel, numFeatures, numIterations);

        Factorization facts = svdFactorizer.factorize();

        IF = facts.allItemFeatures();
        UF = facts.allUserFeatures();

        // gets the dense matrix
        IFMatrix = new DenseMatrix(this.IF);
        UFMatrix = new DenseMatrix(this.UF);

        this.X_Prime = UFMatrix.times(IFMatrix.transpose()); // X' = UF * IF

        stop(TIMER_SVD);
        log.debug("{}Factorized...", LoggerUtils.tab());
    }
}
