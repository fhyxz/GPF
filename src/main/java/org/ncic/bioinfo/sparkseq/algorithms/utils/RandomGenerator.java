package org.ncic.bioinfo.sparkseq.algorithms.utils;

import java.util.Random;

/**
 * Author: wbc
 */
public class RandomGenerator {

    private static final long RANDOM_SEED = 47382911L;
    private static Random randomGenerator = new Random(RANDOM_SEED);

    public static Random getRandomGenerator() {
        return randomGenerator;
    }

    public static void resetRandomGenerator() {
        randomGenerator.setSeed(RANDOM_SEED);
    }

    public static void resetRandomGenerator(long seed) {
        randomGenerator.setSeed(seed);
    }
}
