package org.ncic.bioinfo.sparkseq.algorithms.data.vcf;

import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLoc;
import org.ncic.bioinfo.sparkseq.algorithms.data.reference.RefMetaDataTracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: wbc
 */
public class RefMetaTrackerTraverser {

    private List<RODTraverser> traverserList = new ArrayList<>();

    public RefMetaTrackerTraverser(List<RODContentProvider> providerList) {
        providerList.forEach(provider -> traverserList.add(new RODTraverser(provider)));
    }

    public RefMetaDataTracker getOverlappedTracker(GenomeLoc loc) {
        final List<RODRecordList> bindings = new ArrayList<>(traverserList.size());
        traverserList.forEach(traverse -> bindings.add(traverse.getOverlap(loc)));
        return new RefMetaDataTracker(bindings);
    }
}
