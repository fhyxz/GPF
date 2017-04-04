package org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.graphs;

/**
 * simple node class for storing kmer sequences
 *
 * User: ebanks, mdepristo
 * Date: Mar 23, 2011
 */
public class DeBruijnVertex extends BaseVertex {
    private final static byte[][] sufficesAsByteArray = new byte[256][];
    static {
        for ( int i = 0; i < sufficesAsByteArray.length; i++ )
            sufficesAsByteArray[i] = new byte[]{(byte)(i & 0xFF)};
    }

    public DeBruijnVertex( final byte[] sequence ) {
        super(sequence);
    }

    /**
     * Get the kmer size for this DeBruijnVertex
     * @return integer >= 1
     */
    public int getKmerSize() {
        return sequence.length;
    }

    /**
     * Get the string representation of the suffix of this DeBruijnVertex
     * @return a non-null non-empty string
     */
    public String getSuffixString() {
        return new String(getSuffixAsArray());
    }

    /**
     * Get the suffix byte of this DeBruijnVertex
     *
     * The suffix byte is simply the last byte of the kmer sequence, so if this is holding sequence ACT
     * getSuffix would return T
     *
     * @return a byte
     */
    public byte getSuffix() {
        return sequence[getKmerSize() - 1];
    }

    /**
     * Optimized version that returns a byte[] for the single byte suffix of this graph without allocating memory.
     *
     * Should not be modified
     *
     * @return a byte[] that contains 1 byte == getSuffix()
     */
    private byte[] getSuffixAsArray() {
        return sufficesAsByteArray[getSuffix()];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getAdditionalSequence(boolean source) {
        return source ? super.getAdditionalSequence(source) : getSuffixAsArray();
    }
}
