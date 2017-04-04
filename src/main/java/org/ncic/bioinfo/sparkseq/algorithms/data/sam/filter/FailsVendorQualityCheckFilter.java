package org.ncic.bioinfo.sparkseq.algorithms.data.sam.filter;

import htsjdk.samtools.SAMRecord;

/**
 * Author: wbc
 */
public class FailsVendorQualityCheckFilter extends Filter {
    @Override
    public boolean filterOut(SAMRecord read){
        return read.getReadFailsVendorQualityCheckFlag();
    }
}
