/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.common;import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * Random stuff for projects
 * <p>
 * This random uses a seed to generate the same sequence of random numbers.
 * This lets us make all randomness in the project predictable,
 * if desired, for when we run unit tests, which should be repeatable.
 */
@Slf4j
@UtilityClass
public class RandomUtils {

    @Getter
    private long SEED = 141982;

    private final Random random;

    static {
        random = new Random(SEED);

        log.trace("{}Created a Random object [seed={}]", LoggerUtils.tab(), SEED);
    }

    public void setSeed(long seed) {
        SEED = seed;
        random.setSeed(SEED);

        log.debug("{}Reset the Random object with new seed [seed={}]", LoggerUtils.tab(), SEED);
    }

    public int getRandomInt(int bound) {
        return random.nextInt(bound);
    }

    public int getRandomInt(int bound, int offset) {
        return random.nextInt(bound) + offset;
    }

    public double getRandomDouble(double bound) {
        return random.nextDouble() * bound;
    }

    public double getRandomDouble(double bound, double offset) {
        return random.nextDouble() * bound + offset;
    }

    public boolean getRandomBoolean() {
        return random.nextBoolean();
    }
}