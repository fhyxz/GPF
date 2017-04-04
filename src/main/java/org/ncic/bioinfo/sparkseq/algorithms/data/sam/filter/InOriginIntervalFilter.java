package org.ncic.bioinfo.sparkseq.algorithms.data.sam.filter;

import htsjdk.samtools.SAMRecord;
import org.ncic.bioinfo.sparkseq.algorithms.data.reference.RefContentProvider;

/**
 * 只保留ReferenceContentProvider中Origin Interval中的read，用于避免重复计算read的场合。
 * <p>
 * Author: wbc
 */
public class InOriginIntervalFilter extends Filter {

    private final RefContentProvider refContentProvider;

    public InOriginIntervalFilter(RefContentProvider refContentProvider) {
        this.refContentProvider = refContentProvider;
    }

    @Override
    public boolean filterOut(SAMRecord read) {
        return read.getAlignmentStart() < refContentProvider.getOriginStartCoordinate()
                || read.getAlignmentStart() > refContentProvider.getOriginEndCoordinate();
    }
}
