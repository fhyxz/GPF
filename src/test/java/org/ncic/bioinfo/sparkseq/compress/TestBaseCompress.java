package org.ncic.bioinfo.sparkseq.compress;

import org.ncic.bioinfo.sparkseq.algorithms.walker.AbstractTestCase;

/**
 * Author: wbc
 */
public class TestBaseCompress extends AbstractTestCase {
    public void testFastqCompress() {
        String base = "AGCAGAAGT";
        String quals = "GHFHHGDDA";
        byte[] compressed = BaseCompressTools.compressBase(base.getBytes(), quals.getBytes());
        byte[] depressed = BaseCompressTools.decompressBase(compressed, quals.getBytes());
        assertEquals(base, new String(depressed));
    }

    public void testFastqCompressWithN() {
        String base = "AGCAGNAGT";
        String quals = "GHFHHGDDA";

        byte[] qualByte = quals.getBytes();
        byte[] compressed = BaseCompressTools.compressBase(base.getBytes(), qualByte);
        byte[] depressed = BaseCompressTools.decompressBase(compressed, qualByte);
        assertEquals(base, new String(depressed));
        assertEquals(33, qualByte[5]);
    }
}
