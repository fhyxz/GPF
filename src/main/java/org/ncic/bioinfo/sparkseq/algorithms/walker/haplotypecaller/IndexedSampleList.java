package org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller;

import org.ncic.bioinfo.sparkseq.algorithms.data.basic.IndexedSet;

import java.util.Collection;

/**
 * Author: wbc
 */
public class IndexedSampleList implements SampleList {

    private final IndexedSet<String> samples;

    /**
     * Constructs an empty sample-list.
     */
    public IndexedSampleList() {
        samples = new IndexedSet<>(0);
    }

    /**
     * Constructs a sample-list from a collection of samples.
     *
     * <p>
     *     Repeats in the input collection are ignored (just the first occurrence is kept).
     *     Sample names will be sorted based on the traversal order
     *     of the original collection.
     * </p>
     *
     * @param samples input sample collection.
     *
     * @throws IllegalArgumentException if {@code samples} is {@code null} or it contains {@code nulls}.
     */
    public IndexedSampleList(final Collection<String> samples) {
        this.samples = new IndexedSet<>(samples);
    }

    /**
     * Constructs a sample-list from an array of samples.
     *
     * <p>
     *     Repeats in the input array are ignored (just the first occurrence is kept).
     *     Sample names will be sorted based on the traversal order
     *     of the original array.
     * </p>
     *
     * @param samples input sample array.
     *
     * @throws IllegalArgumentException if {@code samples} is {@code null} or it contains {@code nulls}.
     */
    public IndexedSampleList(final String ... samples) {
        this.samples = new IndexedSet<>(samples);
    }

    @Override
    public int sampleCount() {
        return samples.size();
    }

    @Override
    public int sampleIndex(final String sample) {
        return samples.indexOf(sample);
    }

    @Override
    public String sampleAt(int sampleIndex) {
        return samples.get(sampleIndex);
    }
}
