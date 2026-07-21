/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2026
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.common;

/**
 * Determines the split point k used by divide-and-conquer consistency algorithms when
 * partitioning a constraint set into two halves.
 * <p>
 * The strategy sees only the size of the set being split. Every strategy shipped here needs
 * nothing more, so widening the signature to carry the constraints themselves would be
 * speculative generality.
 * <p>
 * Implementations must not reorder constraints; k is applied positionally to the incoming order.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@FunctionalInterface
public interface SplitPointStrategy {

    /**
     * @param size the size of the constraint set being split at the current call
     * @return the split point k; callers clamp it to [1, size-1] where a real split is possible
     */
    int computeK(int size);

    /** Midpoint split - reproduces the historical hard-coded behaviour. */
    SplitPointStrategy MIDPOINT = size -> size / 2;
}
