package org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.genotyper;

import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLoc;
import org.ncic.bioinfo.sparkseq.algorithms.utils.haplotype.Haplotype;
import org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.readlikelihood.ReadLikelihoods;

import java.util.List;
import java.util.TreeSet;

/**
 * Author: wbc
 */
public class MergeVariantsAcrossHaplotypes {
    /**
     * Merge variants across the haplotypes, updating the haplotype event maps and startPos set as appropriate
     *
     * @param haplotypes a list of haplotypes whose events we want to merge
     * @param readLikelihoods map from sample name -> read likelihoods for each haplotype
     * @param startPosKeySet a set of starting positions of all events among the haplotypes
     * @param ref the reference bases
     * @param refLoc the span of the reference bases
     * @return true if anything was merged
     */
    public boolean merge( final List<Haplotype> haplotypes,
                          final ReadLikelihoods<Haplotype> readLikelihoods,
                          final TreeSet<Integer> startPosKeySet,
                          final byte[] ref,
                          final GenomeLoc refLoc ) {
        return false;
    }
}
