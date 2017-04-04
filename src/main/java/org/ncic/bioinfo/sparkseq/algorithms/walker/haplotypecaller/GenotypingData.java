package org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller;

import htsjdk.variant.variantcontext.Allele;
import org.ncic.bioinfo.sparkseq.algorithms.utils.SampleListUtils;
import org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.readlikelihood.ReadLikelihoods;

/**
 * Author: wbc
 */
public class GenotypingData<A extends Allele> implements SampleList, AlleleList<A> {

    private final PloidyModel ploidyModel;

    private final ReadLikelihoods<A> likelihoods;

    /**
     * Constructs a new genotyping-data collection providing the ploidy model to apply to the input model
     * and the read-likelihoods collection.
     *
     *
     * @param ploidyModel the ploidy model.
     * @param likelihoods the read-likelihoods collection.
     *
     * @throws IllegalArgumentException if either {@code ploidyModel} or {@code likelihoods} is {@code null},
     *   or they are not compatible in terms of the samples they contain; their lists must match.
     */
    public GenotypingData(final PloidyModel ploidyModel, final ReadLikelihoods<A> likelihoods) {
        if (ploidyModel == null)
            throw new IllegalArgumentException("the ploidy model cannot be null");
        if (likelihoods == null)
            throw new IllegalArgumentException("the likelihood object cannot be null");
        this.ploidyModel = ploidyModel;
        this.likelihoods = likelihoods;
        if (!SampleListUtils.equals(ploidyModel, likelihoods))
            throw new IllegalArgumentException("sample list are different between ploidy-model and read-likelihood collection, perhaps just the order");
    }

    /**
     * Returns the ploidy model that corresponds to the data provided.
     * @return never {@code null}.
     */
    public PloidyModel ploidyModel() {
        return ploidyModel;
    }

    @Override
    public int sampleCount() {
        return ploidyModel.sampleCount();
    }

    @Override
    public int sampleIndex(final String sample) {
        return ploidyModel.sampleIndex(sample);
    }

    @Override
    public String sampleAt(int sampleIndex) {
        return ploidyModel.sampleAt(sampleIndex);
    }

    /**
     * Returns read-likelihoods to use for genotyping.
     * @return never {@code null}.
     */
    public ReadLikelihoods<A> readLikelihoods() {
        return likelihoods;
    }

    @Override
    public int alleleCount() {
        return likelihoods.alleleCount();
    }

    @Override
    public int alleleIndex(final A allele) {
        return likelihoods.alleleIndex(allele);
    }

    @Override
    public A alleleAt(final int index) {
        return likelihoods.alleleAt(index);
    }
}
