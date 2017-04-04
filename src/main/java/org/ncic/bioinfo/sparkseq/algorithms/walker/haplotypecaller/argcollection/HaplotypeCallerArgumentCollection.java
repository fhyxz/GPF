package org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.argcollection;

import org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.model.ReferenceConfidenceMode;

/**
 * Author: wbc
 */
public class HaplotypeCallerArgumentCollection extends StandardCallerArgumentCollection {

    public boolean DEBUG;

    public boolean USE_FILTERED_READ_MAP_FOR_ANNOTATIONS = false;

    /**
     * The reference confidence mode makes it possible to emit a per-bp or summarized confidence estimate for a site being strictly homozygous-reference.
     * See http://www.broadinstitute.org/gatk/guide/article?id=2940 for more details of how this works.
     * Note that if you set -ERC GVCF, you also need to set -variant_index_type LINEAR and -variant_index_parameter 128000 (with those exact values!).
     * This requirement is a temporary workaround for an issue with index compression.
     */
    public ReferenceConfidenceMode emitReferenceConfidence = ReferenceConfidenceMode.NONE;

    @Override
    public HaplotypeCallerArgumentCollection clone() {
        return (HaplotypeCallerArgumentCollection) super.clone();
    }

}
