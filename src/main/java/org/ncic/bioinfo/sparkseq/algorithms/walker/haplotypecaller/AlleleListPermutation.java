package org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller;

import htsjdk.variant.variantcontext.Allele;
import org.ncic.bioinfo.sparkseq.algorithms.data.basic.Permutation;

/**
 * Author: wbc
 */
public interface AlleleListPermutation<A extends Allele> extends Permutation<A>, AlleleList<A> {
}
