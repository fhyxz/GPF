package org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller;

import org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.graphs.SeqGraph;
import org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.readthreading.ReadThreadingGraph;

/**
 * Author: wbc
 */
public class AssemblyResult {
    private final Status status;
    private ReadThreadingGraph threadingGraph;
    private final SeqGraph graph;

    /**
     * Create a new assembly result
     * @param status the status, cannot be null
     * @param graph the resulting graph of the assembly, can only be null if result is failed
     */
    public AssemblyResult(final Status status, final SeqGraph graph) {
        if ( status == null ) throw new IllegalArgumentException("status cannot be null");
        if ( status != Status.FAILED && graph == null ) throw new IllegalArgumentException("graph is null but status is " + status);

        this.status = status;
        this.graph = graph;
    }

    /**
     * Returns the threading-graph associated with this assembly-result.
     */
    public void setThreadingGraph(final ReadThreadingGraph threadingGraph) {
        this.threadingGraph = threadingGraph;
    }

    public ReadThreadingGraph getThreadingGraph() {
        return threadingGraph;
    }

    public Status getStatus() { return status; }
    public SeqGraph getGraph() { return graph; }

    public int getKmerSize() {
        return graph.getKmerSize();
    }


    /**
     * Status of the assembly result
     */
    public enum Status {
        /** Something went wrong, and we couldn't produce a meaningful graph */
        FAILED,
        /** Assembly succeeded, but graph degenerated into just the reference sequence */
        JUST_ASSEMBLED_REFERENCE,
        /** Assembly succeeded, and the graph has some meaningful structure */
        ASSEMBLED_SOME_VARIATION
    }
}
