package org.ncic.bioinfo.sparkseq.algorithms.walker.indelrealigner;

import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLoc;
import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLocParser;
import org.ncic.bioinfo.sparkseq.algorithms.utils.HasGenomeLocation;
import org.ncic.bioinfo.sparkseq.algorithms.data.reference.RefContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.data.reference.ReferenceContext;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.GATKSAMRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: wbc
 */
class ReadBin implements HasGenomeLocation {

    private final ArrayList<GATKSAMRecord> reads = new ArrayList<GATKSAMRecord>();
    private byte[] reference = null;
    private GenomeLoc loc = null;
    private final GenomeLocParser parser;
    private final int referencePadding;

    public ReadBin(final GenomeLocParser parser, final int referencePadding) {
        this.parser = parser;
        this.referencePadding = referencePadding;
    }

    // Return false if we can't process this read bin because the reads are not correctly overlapping.
    // This can happen if e.g. there's a large known indel with no overlapping reads.
    public void add(GATKSAMRecord read) {

        final int readStart = read.getSoftStart();
        final int readStop = read.getSoftEnd();
        if ( loc == null )
            loc = parser.createGenomeLoc(read.getReferenceName(), readStart, Math.max(readStop, readStart)); // in case it's all an insertion
        else if ( readStop > loc.getStop() )
            loc = parser.createGenomeLoc(loc.getContig(), loc.getStart(), readStop);

        reads.add(read);
    }

    public List<GATKSAMRecord> getReads() {
        return reads;
    }

    public byte[] getReference(RefContentProvider refContentProvider) {
        // set up the reference if we haven't done so yet
        if ( reference == null ) {
            ReferenceContext context = refContentProvider.getReferenceContext(loc, referencePadding);
            reference = context.getBases();
            loc = context.getLocus();
        }

        return reference;
    }

    public GenomeLoc getLocation() {
        return loc;
    }

    public int size() {
        return reads.size();
    }

    public void clear() {
        reads.clear();
        reference = null;
        loc = null;
    }

}