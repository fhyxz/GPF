package org.ncic.bioinfo.sparkseq.algorithms.walker;

import htsjdk.tribble.Feature;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFCodec;
import htsjdk.variant.vcf.VCFHeader;
import org.ncic.bioinfo.sparkseq.algorithms.data.reference.RefMetaDataTracker;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.GATKSAMRecord;
import org.ncic.bioinfo.sparkseq.algorithms.data.vcf.*;
import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLoc;
import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLocParser;
import org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.ActiveRegion;
import org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.ActiveRegionMapData;
import org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.HaplotypeCaller;
import org.ncic.bioinfo.sparkseq.data.basic.BasicSamRecord;
import org.ncic.bioinfo.sparkseq.data.basic.VcfRecord;
import org.ncic.bioinfo.sparkseq.transfer.Basic2SAMRecordTransfer;
import org.ncic.bioinfo.sparkseq.transfer.SAMRecord2BasicTransfer;
import org.ncic.bioinfo.sparkseq.transfer.VC2VcfRecordTransfer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wbc
 */
public class SerializableActiveRegionMapData implements Serializable {

    public ActiveRegion activeRegion;   // 不包括reads和GenomeLocusParser
    public List<BasicSamRecord> reads;  // 补偿activeRegion的reads
    public List<VcfRecord> rods;   // 需要转化成meta data，只记录了dbsnp的
    public byte[] fullReferenceWithPadding;
    public byte[] refBases;

    public SerializableActiveRegionMapData(ActiveRegionMapData activeRegionMapData,
                                           SAMRecord2BasicTransfer samTransfer,
                                           VC2VcfRecordTransfer vcTransfer) {
        this.activeRegion = activeRegionMapData.activeRegion;
        this.fullReferenceWithPadding = activeRegionMapData.fullReferenceWithPadding;
        this.refBases = activeRegionMapData.refBases;

        this.reads = new ArrayList<>(activeRegion.getReads().size());
        for (GATKSAMRecord record : activeRegion.getReads()) {
            this.reads.add(samTransfer.transfer(record));
        }

        // rods
        List<VariantContext> vcs = getVCInTrackerInLocus(activeRegionMapData.tracker, activeRegion.getExtendedLoc());
        this.rods = new ArrayList<>(vcs.size());
        for (VariantContext vc : vcs) {
            this.rods.add(vcTransfer.transfer(vc, false));
        }
    }

    private List<VariantContext> getVCInTrackerInLocus(final RefMetaDataTracker tracker, final GenomeLoc loc) {
        List<Feature> features = tracker.getValues(HaplotypeCaller.dbsnp.dbsnp.getName());
        List<VariantContext> vcs = new ArrayList<>(features.size());
        for (Feature feature : features) {
            vcs.add((VariantContext) ((GATKFeature.TribbleGATKFeature) feature).getUnderlyingObject());
        }
        return vcs;
    }

    public ActiveRegionMapData toActiveRegionMapData(GenomeLocParser parser,
                                                     Basic2SAMRecordTransfer samTransfer,
                                                     VCFHeader vcfFileHeader,
                                                     VCFCodec codec) {
        List<GATKSAMRecord> gatksamRecords = new ArrayList<>(reads.size());
        for (BasicSamRecord record : reads) {
            gatksamRecords.add(new GATKSAMRecord(samTransfer.transfer(record)));
        }
        this.activeRegion.setReads(gatksamRecords);
        this.activeRegion.setGenomeLocParser(parser);

        List<VariantContext> vcs = new ArrayList<>(rods.size());
        for (VcfRecord vcfRecord : rods) {
            vcs.add(codec.decode(vcfRecord.toString()));
        }
        RODContentProvider dbsnpProvider = new RODContentProvider(RODNames.DBSNP, vcfFileHeader, vcs, parser);
        RODTraverser traverser = new RODTraverser(dbsnpProvider);
        final List<RODRecordList> bindings = new ArrayList<>(1);
        bindings.add(traverser.getOverlap(activeRegion.getExtendedLoc()));
        RefMetaDataTracker tracker = new RefMetaDataTracker(bindings);

        return new ActiveRegionMapData(activeRegion, tracker, fullReferenceWithPadding, refBases);
    }
}
