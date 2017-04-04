package org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller;

import htsjdk.variant.variantcontext.Allele;

/**
 * Author: wbc
 */
public interface AlleleList<A extends Allele> {

    public int alleleCount();

    public int alleleIndex(final A allele);

    public A alleleAt(final int index);

}

