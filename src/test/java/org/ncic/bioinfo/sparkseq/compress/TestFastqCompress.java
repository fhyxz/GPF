package org.ncic.bioinfo.sparkseq.compress;

import junit.framework.TestCase;

/**
 * Author: wbc
 */
public class TestFastqCompress extends TestCase {

    public void testFastqCompress() {
        byte[] sequence = "TGGGATGAGAGCATGAGAAGGTGGAGCTAAGGTGGGAGACCGTCTACCCCCGACCCTGTGTGGTGCACTGACCGTGACTCTCTGCACCTTCTCGTGGGGGA".getBytes();
        byte[] quality = "DCDE8EFFFFBEEEEFGGGGEFEGGGFGGGFF6FF/>@A?EEEE=DCAC5C@@@B=8B64A########################################".getBytes();

        byte[] compressSequenceBytes = BaseCompressTools.compressBase(sequence, quality);
        byte[] compressQualityBytes = QualityCompressTools.compressQual(quality);


        byte[] decompressQual = QualityCompressTools.deCompressQual(compressQualityBytes);
        byte[] decompressSeq = BaseCompressTools.decompressBase(compressSequenceBytes, decompressQual);

        assertEquals(new String(quality), new String(decompressQual));
        assertEquals(new String(sequence), new String(decompressSeq));
    }
}
