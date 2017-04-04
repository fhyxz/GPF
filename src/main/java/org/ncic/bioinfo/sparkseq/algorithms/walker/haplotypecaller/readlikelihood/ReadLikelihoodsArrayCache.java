package org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.readlikelihood;

import java.util.HashMap;

/**
 * Author: wbc
 */
public class ReadLikelihoodsArrayCache {
    private HashMap<Integer, double[][]> cache = new HashMap<>();

    public double[][] getDoubleArray(int alleleCount, int sampleReadCount) {
        double[][] array = cache.getOrDefault(sampleReadCount, null);
        if (array == null) {
            array = new double[alleleCount][sampleReadCount];
            cache.put(sampleReadCount, array);
        }
        return array;
    }
}
