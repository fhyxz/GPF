package org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.graphs;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * General iterator that can iterate over all vertices in a BaseGraph, following either
 * incoming, outgoing edge (as well as both or none) edges.  Supports traversal of graphs
 * with cycles and other crazy structures.  Will only ever visit each vertex once.  The
 * order in which the vertices are visited is undefined.
 *
 * User: depristo
 * Date: 3/24/13
 * Time: 4:41 PM
 */
public class BaseGraphIterator<T extends BaseVertex, E extends BaseEdge> implements Iterator<T>, Iterable<T> {
    final HashSet<T> visited = new HashSet<T>();
    final LinkedList<T> toVisit = new LinkedList<T>();
    final BaseGraph<T,E> graph;
    final boolean followIncomingEdges, followOutgoingEdges;

    /**
     * Create a new BaseGraphIterator starting its traversal at start
     *
     * Note that if both followIncomingEdges and followOutgoingEdges are false, we simply return the
     * start vertex
     *
     * @param graph the graph to iterator over.  Cannot be null
     * @param start the vertex to start at.  Cannot be null
     * @param followIncomingEdges should we follow incoming edges during our
     *                            traversal? (goes backward through the graph)
     * @param followOutgoingEdges should we follow outgoing edges during out traversal?
     */
    public BaseGraphIterator(final BaseGraph<T,E> graph, final T start,
                             final boolean followIncomingEdges, final boolean followOutgoingEdges) {
        if ( graph == null ) throw new IllegalArgumentException("graph cannot be null");
        if ( start == null ) throw new IllegalArgumentException("start cannot be null");
        if ( ! graph.containsVertex(start) ) throw new IllegalArgumentException("start " + start + " must be in graph but it isn't");
        this.graph = graph;
        this.followIncomingEdges = followIncomingEdges;
        this.followOutgoingEdges = followOutgoingEdges;

        toVisit.add(start);
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return ! toVisit.isEmpty();
    }

    @Override
    public T next() {
        final T v = toVisit.pop();

        if ( ! visited.contains(v) ) {
            visited.add(v);
            if ( followIncomingEdges ) for ( final T prev : graph.incomingVerticesOf(v) ) toVisit.add(prev);
            if ( followOutgoingEdges ) for ( final T next : graph.outgoingVerticesOf(v) ) toVisit.add(next);
        }

        return v;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Doesn't implement remove");
    }
}
