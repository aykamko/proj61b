package graph;

import java.util.Comparator;

import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

/** Implements a generalized traversal of a graph.  At any given time,
 *  there is a particular set of untraversed vertices---the "fringe."
 *  Traversal consists of repeatedly removing an untraversed vertex
 *  from the fringe, visting it, and then adding its untraversed
 *  successors to the fringe.  The client can dictate an ordering on
 *  the fringe, determining which item is next removed, by which kind
 *  of traversal is requested.
 *     + A depth-first traversal treats the fringe as a list, and adds
 *       and removes vertices at one end.  It also revisits the node
 *       itself after traversing all successors by calling the
 *       postVisit method on it.
 *     + A breadth-first traversal treats the fringe as a list, and adds
 *       and removes vertices at different ends.  It also revisits the node
 *       itself after traversing all successors as for depth-first
 *       traversals.
 *     + A general traversal treats the fringe as an ordered set, as
 *       determined by a Comparator argument.  There is no postVisit
 *       for this type of traversal.
 *  As vertices are added to the fringe, the traversal calls a
 *  preVisit method on the vertex.
 *
 *  Generally, the client will extend Traversal, overriding the visit,
 *  preVisit, and postVisit methods, as desired (by default, they do nothing).
 *  Any of these methods may throw StopException to halt the traversal
 *  (temporarily, if desired).  The preVisit method may throw a
 *  RejectException to prevent a vertex from being added to the
 *  fringe, and the visit method may throw a RejectException to
 *  prevent its successors from being added to the fringe.
 *  @author Aleks Kamko
 */
public class Traversal<VLabel, ELabel> {

    /** Perform a traversal of G over all vertices reachable from V.
     *  ORDER determines the ordering in which the fringe of
     *  untraversed vertices is visited.  The effect of specifying an
     *  ORDER whose results change as a result of modifications made during the
     *  traversal is undefined. */
    public void traverse(Graph<VLabel, ELabel> G,
                         Graph<VLabel, ELabel>.Vertex v0,
                         Comparator<VLabel> order) {
        _graph = G;
        _lastTraversalType = TraversalTypes.GENERAL;
        _lastOrder = order;
        TreeSet<Graph<VLabel, ELabel>.Vertex> fringe;
        fringe = new TreeSet<Graph<VLabel, ELabel>.Vertex>
                 (new VertexComparator(order));
        fringe.add(v0);
        if (!_continued) {
            _marked = new HashSet<Graph<VLabel, ELabel>.Vertex>();
        }
        _continued = false;

        Graph<VLabel, ELabel>.Vertex v, u;
        Graph<VLabel, ELabel>.Edge e;
        List<Graph<VLabel, ELabel>.Vertex> vlist;
        List<Graph<VLabel, ELabel>.Edge> elist;
        vlist = new ArrayList<Graph<VLabel, ELabel>.Vertex>();
        elist = new ArrayList<Graph<VLabel, ELabel>.Edge>();
        vlist.add(v0);
        elist.add(null);
        int edgeIndex;
        while (!fringe.isEmpty()) {
            v = fringe.pollFirst();
            System.out.println(v);
            edgeIndex = vlist.indexOf(v);
            e = elist.get(edgeIndex);
            vlist.remove(edgeIndex);
            elist.remove(edgeIndex);

            _marked.add(v);
            _finalEdge = e;
            _finalVertex = v;
            visit(v);
            for (Graph<VLabel, ELabel>.Edge d : G.outEdges(v)) {
                u = d.getV(v);
                if (!_marked.contains(u)) {
                    preVisit(d, u);
                    elist.add(d);
                    vlist.add(u);
                    fringe.add(u);
                }
            }
        }
    }

    /** Performs a depth-first traversal of G over all vertices
     *  reachable from V.  That is, the fringe is a sequence and
     *  vertices are added to it or removed from it at one end in
     *  an undefined order.  After the traversal of all successors of
     *  a node is complete, the node itself is revisited by calling
     *  the postVisit method on it. */
    public void depthFirstTraverse(Graph<VLabel, ELabel> G,
                                   Graph<VLabel, ELabel>.Vertex v0) {
        _graph = G;
        _lastTraversalType = TraversalTypes.DEPTH;
        LinkedList<Graph<VLabel, ELabel>.Vertex> fringe;
        LinkedList<Graph<VLabel, ELabel>.Edge> estack;
        fringe = new LinkedList<Graph<VLabel, ELabel>.Vertex>();
        estack = new LinkedList<Graph<VLabel, ELabel>.Edge>();
        fringe.addFirst(v0);
        estack.addFirst(null);
        if (!_continued) {
            _marked = new HashSet<Graph<VLabel, ELabel>.Vertex>();
            _processed = new HashSet<Graph<VLabel, ELabel>.Vertex>();
        }
        _continued = false;

        Graph<VLabel, ELabel>.Vertex v, u;
        Graph<VLabel, ELabel>.Edge e;
        int edgeIndex;
        while (!fringe.isEmpty()) {
            v = fringe.pollFirst();
            e = estack.pollFirst();

            if (!_marked.contains(v)) {
                _marked.add(v);
                _finalVertex = v;
                _finalEdge = e;
                visit(v);
                fringe.addFirst(v);
                fringe.addFirst(null);
                for (Graph<VLabel, ELabel>.Edge d : G.outEdges(v)) {
                    u = d.getV(v);
                    if (!_processed.contains(u)) {
                        _finalEdge = d;
                        _finalVertex = u;
                        preVisit(d, u);
                        fringe.addFirst(u);
                        estack.addFirst(d);
                    }
                }
            } else {
                _finalVertex = v;
                postVisit(v);
                _processed.add(v);
            }
        }
    }

    /** Performs a breadth-first traversal of G over all vertices
     *  reachable from V.  That is, the fringe is a sequence and
     *  vertices are added to it at one end and removed from it at the
     *  other in an undefined order.  After the traversal of all successors of
     *  a node is complete, the node itself is revisited by calling
     *  the postVisit method on it. */
    public void breadthFirstTraverse(Graph<VLabel, ELabel> G,
                                     Graph<VLabel, ELabel>.Vertex v0) {
        _graph = G;
        _lastTraversalType = TraversalTypes.BREADTH;
        LinkedList<Graph<VLabel, ELabel>.Vertex> fringe;
        LinkedList<Graph<VLabel, ELabel>.Edge> equeue;
        fringe = new LinkedList<Graph<VLabel, ELabel>.Vertex>();
        equeue = new LinkedList<Graph<VLabel, ELabel>.Edge>();
        fringe.addFirst(v0);
        equeue.addFirst(null);
        if (!_continued) {
            _marked = new HashSet<Graph<VLabel, ELabel>.Vertex>();
            _processed = new HashSet<Graph<VLabel, ELabel>.Vertex>();
        }
        _continued = false;

        Graph<VLabel, ELabel>.Vertex v, u;
        Graph<VLabel, ELabel>.Edge e;
        int edgeIndex;
        while (!fringe.isEmpty()) {
            v = fringe.pollLast();
            e = equeue.pollLast();

            if (!_marked.contains(v)) {
                _marked.add(v);
                _finalVertex = v;
                _finalEdge = e;
                visit(v);
                for (Graph<VLabel, ELabel>.Edge d : G.outEdges(v)) {
                    u = d.getV(v);
                    if (!_processed.contains(u)) {
                        _finalEdge = d;
                        _finalVertex = u;
                        preVisit(d, u);
                        fringe.addFirst(u);
                        equeue.addFirst(d);
                    }
                }
                fringe.addFirst(v);
                equeue.addFirst(null);
            } else {
                _finalVertex = v;
                postVisit(v);
                _processed.add(v);
            }
        }
    }

    /** Continue the previous traversal starting from V.
     *  Continuing a traversal means that we do not traverse
     *  vertices that have been traversed previously. */
    public void continueTraversing(Graph<VLabel, ELabel>.Vertex v) {
        _continued = true;
        switch (_lastTraversalType) {
            case GENERAL:
                traverse(theGraph(), v, _lastOrder);
                break;
            case DEPTH:
                depthFirstTraverse(theGraph(), v);
                break;
            case BREADTH:
                breadthFirstTraverse(theGraph(), v);
                break;
            default:
                _continued = false;
        }
    }

    /** If the traversal ends prematurely, returns the Vertex argument to
     *  preVisit, visit, or postVisit that caused a Visit routine to
     *  return false.  Otherwise, returns null. */
    public Graph<VLabel, ELabel>.Vertex finalVertex() {
        return _finalVertex;
    }

    /** If the traversal ends prematurely, returns the Edge argument to
     *  preVisit that caused a Visit routine to return false. If it was not
     *  an edge that caused termination, returns null. */
    public Graph<VLabel, ELabel>.Edge finalEdge() {
        return _finalEdge;
    }

    /** Returns the last graph argument to a traverse routine, or null if none
     *  of these methods have been called. */
    protected Graph<VLabel, ELabel> theGraph() {
        return _graph;
    }

    /** Method to be called when adding the node at the other end of E from V0
     *  to the fringe. If this routine throws a StopException,
     *  the traversal ends.  If it throws a RejectException, the edge
     *  E is not traversed. The default does nothing.
     */
    protected void preVisit(Graph<VLabel, ELabel>.Edge e,
                            Graph<VLabel, ELabel>.Vertex v0) {
    }

    /** Method to be called when visiting vertex V.  If this routine throws
     *  a StopException, the traversal ends.  If it throws a RejectException,
     *  successors of V do not get visited from V. The default does nothing. */
    protected void visit(Graph<VLabel, ELabel>.Vertex v) {
    }

    /** Method to be called immediately after finishing the traversal
     *  of successors of vertex V in pre- and post-order traversals.
     *  If this routine throws a StopException, the traversal ends.
     *  Throwing a RejectException has no effect. The default does nothing.
     */
    protected void postVisit(Graph<VLabel, ELabel>.Vertex v) {
    }

    /** The Vertex (if any) that terminated the last traversal. */
    protected Graph<VLabel, ELabel>.Vertex _finalVertex;
    /** The Edge (if any) that terminated the last traversal. */
    protected Graph<VLabel, ELabel>.Edge _finalEdge;
    /** The last graph traversed. */
    protected Graph<VLabel, ELabel> _graph;

    /** Already visited vertices in the last traversal. */
    private Set<Graph<VLabel, ELabel>.Vertex> _marked;
    /** Already postVisited vertices in the last traversal. */
    private Set<Graph<VLabel, ELabel>.Vertex> _processed;
    /** Type of last traversal. */
    private TraversalTypes _lastTraversalType;
    /** Order of last general traversal. */
    private Comparator<VLabel> _lastOrder;
    /** Types of traversals. */
    private enum TraversalTypes {
        GENERAL, DEPTH, BREADTH;
    }
    /** Flag for continued traversal. (false by default.) */
    private boolean _continued = false;

    /** Comparator that orders vertices by their VLabels based on a given 
     *  VLabel Comparator. */
    private class VertexComparator
            implements Comparator<Graph<VLabel, ELabel>.Vertex> {
        VertexComparator(Comparator<VLabel> comparator) {
            _comparator = comparator;
        }

        @Override
        public int compare(Graph<VLabel, ELabel>.Vertex v0,
            Graph<VLabel, ELabel>.Vertex v1) {
            return _comparator.compare(v0.getLabel(), v1.getLabel());
        }

        /** VLabel Comparator to base order off of. */
        private final Comparator<VLabel> _comparator;
    }

}
