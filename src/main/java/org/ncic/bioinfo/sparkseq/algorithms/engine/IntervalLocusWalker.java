package org.ncic.bioinfo.sparkseq.algorithms.engine;

import org.ncic.bioinfo.sparkseq.algorithms.data.reference.RefContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.data.reference.RefMetaDataTracker;
import org.ncic.bioinfo.sparkseq.algorithms.data.reference.ReferenceContext;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.AlignmentContext;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.IntervalLocusSamTraverser;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.SamContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.filter.Filter;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.filter.FilterUtils;
import org.ncic.bioinfo.sparkseq.algorithms.data.vcf.RODContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.data.vcf.RefMetaTrackerTraverser;
import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLoc;
import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLocParser;

import java.util.List;

/**
 * Author: wbc
 */
public abstract class IntervalLocusWalker extends LocusWalker {

    protected IntervalLocusSamTraverser intervalSamTraverser = null;
    protected RefMetaTrackerTraverser refMetaTrackerTraverser = null;
    protected List<GenomeLoc> intervals = null;

    public IntervalLocusWalker(GenomeLocParser genomeLocParser,
                               RefContentProvider refContentProvider,
                               SamContentProvider samContentProvider,
                               List<RODContentProvider> rodContentProviderList,
                               List<GenomeLoc> intervals) {
        super(genomeLocParser, refContentProvider, samContentProvider, rodContentProviderList);
        this.intervals = intervals;
    }

    public void run() {
        initialize();

        intervalSamTraverser = getIntervalLocusSamTraverser();
        refMetaTrackerTraverser = new RefMetaTrackerTraverser(rodContentProviderList);
        intervalSamTraverser.rewind();

        while (intervalSamTraverser.hasNext()) {
            AlignmentContext alignmentContext = intervalSamTraverser.next();
            GenomeLoc locus = alignmentContext.getLocation();
            ReferenceContext referenceContext =
                    refContentProvider.getReferenceContext(locus);
            RefMetaDataTracker tracker = refMetaTrackerTraverser.getOverlappedTracker(locus);
            map(tracker, referenceContext, alignmentContext);
        }

        onTraversalDone();
    }

    protected IntervalLocusSamTraverser getIntervalLocusSamTraverser() {
        FilterUtils filterUtils = getLocusWalkerFilterUtils();
        List<Filter> filters = getFilter();
        if (filters != null) {
            for (Filter filter : filters) {
                filterUtils.addFilter(filter);
            }
        }

        GenomeLoc allLocus = refContentProvider.getLocus();
        IntervalLocusSamTraverser traverser = new IntervalLocusSamTraverser(samContentProvider, allLocus, intervals, filterUtils);
        traverser.rewind();
        return traverser;
    }
}
