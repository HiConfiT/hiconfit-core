/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.monitor;

public interface IAnalysisMonitor {
    boolean isDone();
    void setNumberOfTasks(int numberOfTasks);
    int getRemainingTasks();
    void done();
    void doneAll();
    void reset();
    void printProgress();
}
