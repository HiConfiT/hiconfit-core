/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2026
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.common;

import lombok.Getter;

/**
 * Splits at a fixed fraction of the set size: k = floor(ratio * size).
 * <p>
 * {@code ratio} is a plain scalar. This class attaches no meaning to it and does not know how it
 * was derived; callers own that.
 * <p>
 * Flooring makes ratio 0.5 exactly equivalent to {@link SplitPointStrategy#MIDPOINT} at every
 * size, since {@code floor(0.5 * size) == size / 2}.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Getter
public final class RatioSplitPointStrategy implements SplitPointStrategy {

    private final double ratio;

    public RatioSplitPointStrategy(double ratio) {
        this.ratio = ratio;
    }

    @Override
    public int computeK(int size) {
        return (int) Math.floor(ratio * size);
    }
}
