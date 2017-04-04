package org.ncic.bioinfo.sparkseq.algorithms.engine;

import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLocParser;
import org.ncic.bioinfo.sparkseq.algorithms.data.reference.RefContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.data.reference.RefMetaDataTracker;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.SamContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.data.vcf.RODContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.ActiveRegion;
import org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.ActiveRegionMapData;

import java.util.List;

/**
 * Author: wbc
 */
public abstract class ActiveRegionWalker extends Walker {

    List<ActiveRegionMapData> activeRegionMapDataList = null;

    public ActiveRegionWalker(GenomeLocParser genomeLocParser,
                              RefContentProvider refContentProvider,
                              SamContentProvider samContentProvider,
                              List<RODContentProvider> rodContentProviderList,
                              List<ActiveRegionMapData> activeRegionMapDataList) {
        super(genomeLocParser, refContentProvider, samContentProvider, rodContentProviderList);
        this.activeRegionMapDataList = activeRegionMapDataList;
    }

    public void run() {

        for (ActiveRegionMapData activeRegionMapData : activeRegionMapDataList) {
            map(activeRegionMapData);
        }

        onTraversalDone();
    }

    protected abstract void map(ActiveRegionMapData activeRegionMapData);

    protected abstract void onTraversalDone();

}
