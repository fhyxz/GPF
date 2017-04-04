package org.ncic.bioinfo.sparkseq.algorithms.data.sam.filter;

import htsjdk.samtools.SAMRecord;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.GATKSAMRecord;

/**
 * @author wbc
 */
public class NegativeAlignmentStartFilter extends Filter {

    @Override
    public boolean filterOut(SAMRecord read) {
        return read.getAlignmentStart() <= 0 || ((GATKSAMRecord)read).getSoftStart() <= 0;
    }
}
