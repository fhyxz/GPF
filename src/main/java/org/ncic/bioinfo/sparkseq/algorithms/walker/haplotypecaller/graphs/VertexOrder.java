package org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.graphs;

/**
* Indicate a SeqGraph vertex topological order between to vertices.
*/
public enum VertexOrder {
    BEFORE, AFTER, SAME, PARALLEL;

    public VertexOrder inverse() {
        switch (this) {
            case BEFORE: return AFTER;
            case AFTER: return BEFORE;
            default: return this;
        }
    }

}
