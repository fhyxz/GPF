package org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.readthreading;

/**
 * Keeps track of the information needed to add a sequence to the read threading assembly graph
 *
 * User: depristo
 * Date: 4/18/13
 * Time: 8:59 AM
 * To change this template use File | Settings | File Templates.
 */
final class SequenceForKmers {
    final String name;
    final byte[] sequence;
    final int start, stop;
    final int count;
    final boolean isRef;

    /**
     * Create a new sequence for creating kmers
     */
    SequenceForKmers(final String name, byte[] sequence, int start, int stop, int count, boolean ref) {
        if ( start < 0 ) throw new IllegalArgumentException("Invalid start " + start);
        if ( stop < start ) throw new IllegalArgumentException("Invalid stop " + stop);
        if ( sequence == null ) throw new IllegalArgumentException("Sequence is null ");
        if ( count < 1 ) throw new IllegalArgumentException("Invalid count " + count);

        this.name = name;
        this.sequence = sequence;
        this.start = start;
        this.stop = stop;
        this.count = count;
        this.isRef = ref;
    }
}
