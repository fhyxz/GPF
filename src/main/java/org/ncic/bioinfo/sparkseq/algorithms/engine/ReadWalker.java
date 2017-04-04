package org.ncic.bioinfo.sparkseq.algorithms.engine;

import org.ncic.bioinfo.sparkseq.algorithms.data.sam.filter.DuplicateReadFilter;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.filter.NegativeAlignmentStartFilter;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.filter.SupplementaryReadFilter;
import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLoc;
import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLocParser;
import org.ncic.bioinfo.sparkseq.algorithms.data.reference.RefContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.data.reference.RefMetaDataTracker;
import org.ncic.bioinfo.sparkseq.algorithms.data.reference.ReferenceContext;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.GATKSAMRecord;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.ReadSamTraverser;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.SamContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.filter.Filter;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.filter.FilterUtils;
import org.ncic.bioinfo.sparkseq.algorithms.data.vcf.RODContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.data.vcf.RefMetaTrackerTraverser;

import java.util.List;

/**
 * Author: wbc
 */
public abstract class ReadWalker extends Walker {

    private ReadSamTraverser readSamTraverser;
    private RefMetaTrackerTraverser refMetaTrackerTraverser;

    public ReadWalker(GenomeLocParser genomeLocParser,
                      RefContentProvider refContentProvider,
                      SamContentProvider samContentProvider,
                      List<RODContentProvider> rodContentProviderList) {
        super(genomeLocParser, refContentProvider, samContentProvider, rodContentProviderList);
    }

    public void run() {
        initialize();

        readSamTraverser = getReadSamTraverser();
        refMetaTrackerTraverser = new RefMetaTrackerTraverser(rodContentProviderList);

        GenomeLoc traverseLoc = refContentProvider.getLocus();
        while (readSamTraverser.hasNext()) {
            GATKSAMRecord gatksamRecord = readSamTraverser.next();
            ReferenceContext referenceContext = null;
            RefMetaDataTracker tracker = null;
            if (gatksamRecord.getReadUnmappedFlag()) {
                referenceContext = null;
                tracker = new RefMetaDataTracker();
            } else {
                GenomeLoc locus = new GenomeLoc(traverseLoc.getContig(), traverseLoc.getContigIndex(),
                        gatksamRecord.getAlignmentStart(), gatksamRecord.getAlignmentEnd());
                referenceContext = refContentProvider.getReferenceContext(locus);
                tracker = refMetaTrackerTraverser.getOverlappedTracker(locus);
            }
            map(referenceContext, gatksamRecord, tracker);
        }
        onTraversalDone();
    }

    private ReadSamTraverser getReadSamTraverser() {
        FilterUtils filterUtils = getReadWalkerFilterUtils();
        List<Filter> filters = getFilter();
        if (filters != null) {
            for (Filter filter : filters) {
                filterUtils.addFilter(filter);
            }
        }
        ReadSamTraverser traverser = new ReadSamTraverser(samContentProvider, filterUtils);
        traverser.rewind();
        return traverser;
    }

    private FilterUtils getReadWalkerFilterUtils() {
        FilterUtils filterUtils = new FilterUtils();
        //filterUtils.addFilter(new DuplicateReadFilter());
        //filterUtils.addFilter(new SupplementaryReadFilter());
        //filterUtils.addFilter(new NegativeAlignmentStartFilter());
        return filterUtils;
    }

    protected abstract List<Filter> getFilter();

    protected abstract void map(final ReferenceContext ref,
                                final GATKSAMRecord originalRead,
                                final RefMetaDataTracker metaDataTracker);

    protected abstract void onTraversalDone();
}
