/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.fma.monitor;

import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;

@NoArgsConstructor
public class ProgressMonitor implements IAnalysisMonitor {

    protected AtomicInteger progress = new AtomicInteger(0);
    protected AtomicInteger numberOfTasks = new AtomicInteger(0);

    @Override
    public boolean isDone() {
        return progress.get() == numberOfTasks.get();
    }

    @Override
    public void setNumberOfTasks(int numberOfTasks) {
        this.numberOfTasks = new AtomicInteger(numberOfTasks);

        System.out.println("Total tasks: " + getRemainingTasks());
        System.out.flush();
    }

    @Override
    public int getRemainingTasks() {
        return numberOfTasks.get() - progress.get();
    }

    @Override
    public void done() {
        if (!isDone()) {
            progress.incrementAndGet();
            printProgress();
        }
    }

    @Override
    public void doneAll() {
        progress.set(numberOfTasks.get());
    }

    @Override
    public void reset() {
        progress.set(0);
    }

    @Override
    public void printProgress() {
        System.out.print("Progress: " + Math.round((double) progress.get() / numberOfTasks.get() * 100) + "%");
        System.out.println(" - Remaining tasks: " + getRemainingTasks());
        System.out.flush();
    }
}
