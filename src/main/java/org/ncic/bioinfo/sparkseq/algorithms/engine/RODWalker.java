package org.ncic.bioinfo.sparkseq.algorithms.engine;

import org.ncic.bioinfo.sparkseq.algorithms.data.reference.RefContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.data.reference.RefMetaDataTracker;
import org.ncic.bioinfo.sparkseq.algorithms.data.reference.ReferenceContext;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.SamContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.data.vcf.RODContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.data.vcf.RefMetaTrackerTraverser;
import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLoc;
import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLocParser;

import java.util.List;

/**
 * Author: wbc
 */
public abstract class RODWalker extends Walker {

    private RefMetaTrackerTraverser refMetaTrackerTraverser;

    public RODWalker(GenomeLocParser genomeLocParser,
                     RefContentProvider refContentProvider,
                     SamContentProvider samContentProvider,
                     List<RODContentProvider> rodContentProviderList) {
        super(genomeLocParser, refContentProvider, samContentProvider, rodContentProviderList);
    }

    public void run() {
        initialize();

        GenomeLoc traverseLoc = refContentProvider.getLocus();
        refMetaTrackerTraverser = new RefMetaTrackerTraverser(rodContentProviderList);

        int contigId = traverseLoc.getContigIndex();
        String contigName = traverseLoc.getContig();
        int start = traverseLoc.getStart();
        int stop = traverseLoc.getStop();
        for (int i = start; i <= stop; i++) {
            GenomeLoc loc = new GenomeLoc(contigName, contigId, i, i);
            RefMetaDataTracker tracker = refMetaTrackerTraverser.getOverlappedTracker(loc);
            ReferenceContext ref = refContentProvider.getReferenceContext(loc);
            map(ref, tracker);
        }

        onTraversalDone();
    }

    protected abstract void map(final ReferenceContext ref,
                                final RefMetaDataTracker metaDataTracker);

    protected abstract void onTraversalDone();
}
