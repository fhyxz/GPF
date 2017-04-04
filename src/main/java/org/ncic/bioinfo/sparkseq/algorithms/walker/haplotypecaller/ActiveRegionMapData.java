package org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller;

import org.ncic.bioinfo.sparkseq.algorithms.data.reference.RefContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.data.reference.RefMetaDataTracker;

/**
 * Data to use in the ActiveRegionWalker.map function produced by the NanoScheduler input iterator
 *
 * Author: wbc
 */
public final class ActiveRegionMapData {
    public ActiveRegion activeRegion;
    public RefMetaDataTracker tracker;
    public byte[] fullReferenceWithPadding;
    public byte[] refBases;

    public ActiveRegionMapData(ActiveRegion activeRegion, RefMetaDataTracker tracker,
                               byte[] fullReferenceWithPadding, byte[] refBases) {
        this.activeRegion = activeRegion;
        this.tracker = tracker;
        this.fullReferenceWithPadding = fullReferenceWithPadding;
        this.refBases = refBases;
    }

    public ActiveRegionMapData(ActiveRegion activeRegion, RefMetaDataTracker tracker,
                               RefContentProvider refContentProvider) {
        this.activeRegion = activeRegion;
        this.tracker = tracker;
        this.fullReferenceWithPadding = activeRegion.getActiveRegionReference(refContentProvider, HaplotypeCaller.REFERENCE_PADDING);
        this.refBases = activeRegion.getActiveRegionReference(refContentProvider);
    }
}