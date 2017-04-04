package org.ncic.bioinfo.sparkseq.algorithms.utils;

/**
 * Indicates that this object has a genomic location and provides a systematic interface to get it.
 *
 * Author: wbc
 */
public interface HasGenomeLocation {
    public GenomeLoc getLocation();
}