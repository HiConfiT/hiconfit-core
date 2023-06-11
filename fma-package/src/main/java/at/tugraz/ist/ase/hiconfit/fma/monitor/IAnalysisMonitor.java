/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fma.monitor;

public interface IAnalysisMonitor {
    boolean isDone();
    void setNumberOfTasks(int numberOfTasks);
    int getRemainingTasks();
    void done();
    void doneAll();
    void reset();
    void printProgress();
}
