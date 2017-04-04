package org.ncic.bioinfo.sparkseq.algorithms.data.reference;

import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLoc;

/**
 * Author: wbc
 */
public class ReferenceContext {

    final int contigId;
    final private GenomeLoc locus;
    final private byte[] bases;

    /**
     * The window of reference information around the current locus.
     */
    final private GenomeLoc window;

    public ReferenceContext(GenomeLoc locus, int contigId, byte[] bases) {
        this.locus = locus;
        this.window = locus;
        this.contigId = contigId;
        this.bases = bases;
    }

    public ReferenceContext(GenomeLoc locus, GenomeLoc window, int contigId, byte[] bases) {
        this.locus = locus;
        this.window = window;
        this.contigId = contigId;
        this.bases = bases;
    }

    /**
     * Contig id of this reference
     *
     * @return contig id
     */
    public int getContigId() {
        return contigId;
    }

    /**
     * The locus currently being examined.
     *
     * @return The current locus.
     */
    public GenomeLoc getLocus() {
        return locus;
    }

    public GenomeLoc getWindow() {
        return window;
    }

    /**
     * Get the base at the given locus.
     *
     * @return The base at the given locus from the reference.
     */
    public byte getBase() {
        return bases[0];
    }

    /**
     * All the bases in the window currently being examined.
     *
     * @return All bases available.  If the window is of size [0,0], the array will
     * contain only the base at the given locus.
     */
    public byte[] getBases() {
        return bases;
    }

    public byte[] getForwardBases() {
        final byte[] bases = getBases();
        final int mid = locus.getStart() - window.getStart();
        // todo -- warning of performance problem, especially if this is called over and over
        return new String(bases).substring(mid).getBytes();
    }

}

