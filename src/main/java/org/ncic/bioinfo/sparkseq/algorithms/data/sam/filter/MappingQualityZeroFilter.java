package org.ncic.bioinfo.sparkseq.algorithms.data.sam.filter;

import htsjdk.samtools.SAMRecord;

/**
 * Author: wbc
 */
public class MappingQualityZeroFilter extends Filter {
    public boolean filterOut(SAMRecord rec) {
        return (rec.getMappingQuality() == 0);
    }
}

