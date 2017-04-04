package org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.graphs;

import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

/**
 * Merges the incoming vertices of a vertex V of a graph
 *
 * Looks at the vertices that are incoming to V (i.e., have an outgoing edge connecting to V).  If
 * they all have the same sequence, merges them into the sequence of V, and updates the graph
 * as appropriate
 *
 * User: depristo
 * Date: 3/22/13
 * Time: 8:31 AM
 */
public class SharedSequenceMerger {
    public SharedSequenceMerger() { }

    /**
     * Attempt to merge the incoming vertices of v
     *
     * @param graph the graph containing the vertex v
     * @param v the vertex whose incoming vertices we want to merge
     * @return true if some useful merging was done, false otherwise
     */
    public boolean merge(final SeqGraph graph, final SeqVertex v) {
        if ( graph == null ) throw new IllegalArgumentException("graph cannot be null");
        if ( ! graph.vertexSet().contains(v) ) throw new IllegalArgumentException("graph doesn't contain vertex " + v);

        final Set<SeqVertex> prevs = graph.incomingVerticesOf(v);
        if ( ! canMerge(graph, v, prevs) )
            return false;
        else {
//            graph.printGraph(new File("csm." + counter + "." + v.getSequenceString() + "_pre.dot"), 0);

            final List<BaseEdge> edgesToRemove = new LinkedList<>();
            final byte[] prevSeq = prevs.iterator().next().getSequence();
            final SeqVertex newV = new SeqVertex(ArrayUtils.addAll(prevSeq, v.getSequence()));
            graph.addVertex(newV);

            for ( final SeqVertex prev : prevs ) {
                for ( final BaseEdge prevIn : graph.incomingEdgesOf(prev) ) {
                    graph.addEdge(graph.getEdgeSource(prevIn), newV, prevIn.copy());
                    edgesToRemove.add(prevIn);
                }
            }

            for ( final BaseEdge e : graph.outgoingEdgesOf(v) ) {
                graph.addEdge(newV, graph.getEdgeTarget(e), e.copy());
            }

            graph.removeAllVertices(prevs);
            graph.removeVertex(v);
            graph.removeAllEdges(edgesToRemove);

//            graph.printGraph(new File("csm." + counter++ + "." + v.getSequenceString() + "_post.dot"), 0);

            return true;
        }
    }

    //private static int counter = 0;

    /**
     * Can we safely merge the incoming vertices of v
     *
     * @param graph the graph containing v and incomingVertices
     * @param v the vertex we want to merge into
     * @param incomingVertices the incoming vertices of v
     * @return true if we can safely merge incomingVertices
     */
    private boolean canMerge(final SeqGraph graph, final SeqVertex v, final Collection<SeqVertex> incomingVertices) {
        if ( incomingVertices.isEmpty() )
            return false;

        final SeqVertex first = incomingVertices.iterator().next();
        for ( final SeqVertex prev : incomingVertices) {
            if ( ! prev.seqEquals(first) )
                // cannot merge if our sequence isn't the same as the first sequence
                return false;
            final Collection<SeqVertex> prevOuts = graph.outgoingVerticesOf(prev);
            if ( prevOuts.size() != 1 )
                // prev -> v must be the only edge from prev
                return false;
            if ( prevOuts.iterator().next() != v )
                // don't allow cyles
                return false;
            if ( graph.inDegreeOf(prev) == 0 )
                // cannot merge when any of the incoming nodes are sources
                return false;
        }

        return true;
    }

}