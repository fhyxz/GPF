package org.ncic.bioinfo.sparkseq.algorithms.utils.smithwaterman;

/**
 * Author: wbc
 */
public enum SWParameterSet {
    // match=1, mismatch = -1/3, gap=-(1+k/3)
    ORIGINAL_DEFAULT(new Parameters(3,-1,-4,-3)),

    /**
     * A standard set of values for NGS alignments
     */
    STANDARD_NGS(new Parameters(25, -50, -110, -6));

    protected Parameters parameters;

    SWParameterSet(final Parameters parameters) {
        if ( parameters == null ) throw new IllegalArgumentException("parameters cannot be null");

        this.parameters = parameters;
    }
}
