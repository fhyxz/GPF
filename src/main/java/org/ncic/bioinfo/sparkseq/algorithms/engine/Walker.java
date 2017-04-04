package org.ncic.bioinfo.sparkseq.algorithms.engine;

import htsjdk.samtools.SAMReadGroupRecord;
import org.apache.log4j.Logger;
import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLocParser;
import org.ncic.bioinfo.sparkseq.algorithms.data.reference.RefContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.SamContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.data.vcf.RODContentProvider;

import java.util.List;

/**
 * Author: wbc
 */
public abstract class Walker {

    final protected static Logger logger = Logger.getLogger(Walker.class);

    protected GenomeLocParser genomeLocParser;
    protected RefContentProvider refContentProvider;
    protected SamContentProvider samContentProvider;
    protected List<RODContentProvider> rodContentProviderList;

    public Walker(GenomeLocParser genomeLocParser,
                  RefContentProvider refContentProvider,
                  SamContentProvider samContentProvider,
                  List<RODContentProvider> rodContentProviderList) {
        this.genomeLocParser = genomeLocParser;
        this.refContentProvider = refContentProvider;
        this.samContentProvider = samContentProvider;
        this.rodContentProviderList = rodContentProviderList;
    }

    protected abstract void initialize();

    public abstract void run();

    public String getSampleName() {
        List<SAMReadGroupRecord> readGroupInfos = samContentProvider.getSamFileHeader().getReadGroups();
        return (readGroupInfos.size() > 0) ? readGroupInfos.get(0).getSample() : "Sample1";
    }

}
