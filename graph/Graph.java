package graph;

import java.util.Comparator;

import java.util.Collections;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;

import java.util.Iterator;

/* Do not add or remove public or protected members, or modify the signatures of
 * any public methods.  You may make changes that don't affect the API as seen
 * from outside the graph package:
 *   + You may make methods in Graph abstract, if you want different
 *     implementations in DirectedGraph and UndirectedGraph.
 *   + You may add bodies to abstract methods, modify existing bodies,
 *     or override inherited methods.
 *   + You may change parameter names, or add 'final' modifiers to parameters.
 *   + You may add private and package private members.
 *   + You may add additional non-public classes to the graph package.
 */

/** Represents a general graph whose vertices are labeled with a type
 *  VLABEL and whose edges are labeled with a type ELABEL. The
 *  vertices are represented by the inner type Vertex and edges by
 *  inner type Edge.  A graph may be directed or undirected.  For
 *  an undirected graph, outgoing and incoming edges are the same.
 *  Graphs may have self edges and may have multiple edges between vertices.
 *
 *  The vertices and edges of the graph, the edges incident on a
 *  vertex, and the neighbors of a vertex are all accessible by
 *  iterators.  Changing the graph's structure by adding or deleting
 *  edges or vertices invalidates these iterators (subsequent use of
 *  them is undefined.)
 *  @author Aleks Kamko
 */
public abstract class Graph<VLabel, ELabel> {

    /** Constant for use in .hashCode methods. */
    private static final int HASH_CONST = 31;

    /** Represents one of my vertices. */
    public class Vertex {

        /** A new vertex with LABEL as the value of getLabel(). */
        Vertex(VLabel label) {
            _label = label;
            _outgoing = new LinkedHashMap<Vertex, LinkedList<Edge>>();
            _incoming = new LinkedHashMap<Vertex, LinkedList<Edge>>();
            _incomingNum = 0;
            _outgoingNum = 0;
        }

        /** Returns the label on this vertex. */
        public VLabel getLabel() {
            return _label;
        }

        /** Adds an outgoing edge EDGE from this vertex. */
        private void addOutgoingEdge(Edge edge) {
            if (addEdgeToMap(edge, _outgoing)) {
                _outgoingNum += 1;
            }
        }

        /** Adds an incoming edge EDGE from this vertex. */
        private void addIncomingEdge(Edge edge) {
            if (addEdgeToMap(edge, _incoming)) {
                _incomingNum += 1;
            }
        }

        /** Adds edge EDGE to MAP. Returns true is successful, and false
         *  otherwise. */
        private boolean addEdgeToMap(Edge edge,
                Map<Vertex, LinkedList<Edge>> map) {
            if (edge == null) {
                return false;
            }
            Vertex other = edge.getV(this);
            LinkedList<Edge> elist = map.get(other);
            if (elist == null) {
                elist = new LinkedList<Edge>();
                map.put(other, elist);
            }
            elist.add(edge);
            return true;
        }

        /** Removes outgoing edge EDGE from this vertex. */
        private void removeOutgoing(Edge edge) {
            if (removeEdgeFromMap(edge, _outgoing)) {
                _outgoingNum -= 1;
            }
        }

        /** Removes incoming edge EDGE from this vertex. */
        private void removeIncoming(Edge edge) {
            if (removeEdgeFromMap(edge, _incoming)) {
                _incomingNum -= 1;
            }
        }

        /** Removes an edge EDGE from map MAP. */
        private boolean removeEdgeFromMap(Edge edge,
                Map<Vertex, LinkedList<Edge>> map) {
            if (edge == null) {
                return false;
            }
            Vertex other = edge.getV(this);
            LinkedList<Edge> elist = map.get(other);
            if (elist != null) {
                elist.remove(edge);
                return true;
            }
            return false;
        }

        /** Returns an iterator over the outgoing edges of this vertex. */
        private Iterator<Edge> outgoingEdges() {
            return new LinkedEdgeIterator(_outgoing.values().iterator());
        }

        /** Returns an iterator over the incoming edges of this vertex. */
        private Iterator<Edge> incomingEdges() {
            return new LinkedEdgeIterator(_incoming.values().iterator());
        }

        /** Returns an iterator over this vertex's successors. */
        private Iterator<Vertex> successors() {
            return _outgoing.keySet().iterator();
        }

        /** Returns an iterator over this vertex's predecessors. */
        private Iterator<Vertex> predecessors() {
            return _incoming.keySet().iterator();
        }

        /** Returns the number of outgoing edges from this vertex. */
        private int outDegree() {
            return _outgoingNum;
        }

        /** Returns the number of incoming edges into this vertex. */
        private int inDegree() {
            return _incomingNum;
        }

        /** Returns true iff this Vertex contains an outgoing edge
         *  with V as its opposite vertex. */
        private boolean containsEdgeTo(Vertex v) {
            return _outgoing.get(v) != null;
        }

        /** Returns true iff this Vertex contains an outgoing edge
         *  with label LABEL with V as its opposite vertex. */
        private boolean containsEdgeToWithLabel(Vertex v, ELabel label) {
            LinkedList<Edge> elist = _outgoing.get(v);
            if (elist != null) {
                for (Edge e : elist) {
                    if (e.getLabel().equals(label)) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return String.valueOf(_label);
        }

        /** Iterates over all edges in the multiple linked lists of a
         *  Map<K, LinkedList<Edge>>. */
        private class LinkedEdgeIterator implements Iterator<Edge> {

            /** Default LinkedEdgeIterator that takes a <LinkedList<Edge>>
             *  iterator LISTITER as input. */
            LinkedEdgeIterator(Iterator<LinkedList<Edge>> listIter) {
                _listIter = listIter;
                _edgeIter = null;
            }

            @Override
            public boolean hasNext() {
                return _listIter.hasNext()
                    || (_edgeIter != null && _edgeIter.hasNext());
            }

            @Override
            public Edge next() {
                if ((_edgeIter == null || !_edgeIter.hasNext())
                        && _listIter.hasNext()) {
                    _edgeIter = _listIter.next().iterator();
                }
                return _edgeIter.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove edges");
            }

            /** Edge iterator. */
            private Iterator<Edge> _edgeIter;
            /** LinkedList iterator. */
            private final Iterator<LinkedList<Edge>> _listIter;
        }

        /** The label on this vertex. */
        private final VLabel _label;
        /** Outgoing edges of this vertex. */
        private final Map<Vertex, LinkedList<Edge>> _outgoing;
        /** Incoming edges of this vertex. */
        private final Map<Vertex, LinkedList<Edge>> _incoming;
        /** Number of incoming edges. */
        private int _incomingNum;
        /** Number of outgoing edges. */
        private int _outgoingNum;
    }

    /** Represents one of my edges. */
    public class Edge {

        /** An edge (V0,V1) with label LABEL.  It is a directed edge (from
         *  V0 to V1) in a directed graph. */
        Edge(Vertex v0, Vertex v1, ELabel label) {
            _label = label;
            _v0 = v0;
            _v1 = v1;
        }

        /** Returns the label on this edge. */
        public ELabel getLabel() {
            return _label;
        }

        /** Return the vertex this edge exits. For an undirected edge, this is
         *  one of the incident vertices. */
        public Vertex getV0() {
            return _v0;
        }

        /** Return the vertex this edge enters. For an undirected edge, this is
         *  the incident vertices other than getV1(). */
        public Vertex getV1() {
            return _v1;
        }

        /** Returns the vertex at the other end of me from V.  */
        public final Vertex getV(Vertex v) {
            if (v == _v0) {
                return _v1;
            } else if (v == _v1) {
                return _v0;
            } else {
                throw new
                    IllegalArgumentException("vertex not incident to edge");
            }
        }

        @Override
        public String toString() {
            return String.format("(%s,%s):%s", _v0, _v1, _label);
        }

        /** Endpoints of this edge.  In directed edges, this edge exits _V0
         *  and enters _V1. */
        private final Vertex _v0, _v1;
        /** The label on this edge. */
        private final ELabel _label;

    }

    /*=====  Methods and variables of Graph =====*/

    /** Constructs an empty graph. */
    Graph() {
        _vertices = new TreeSet<Vertex>(new VertexComparator());
        _edges = new TreeSet<Edge>(new EdgeComparator());
    }

    /** Returns the number of vertices in me. */
    public int vertexSize() {
        return _vertices.size();
    }

    /** Returns the number of edges in me. */
    public int edgeSize() {
        return _edges.size();
    }

    /** Returns true iff I am a directed graph. */
    public abstract boolean isDirected();

    /** Returns the number of outgoing edges incident to V. Assumes V is one of
     *  my vertices.  */
    public int outDegree(Vertex v) {
        return v.outDegree();
    }

    /** Returns the number of incoming edges incident to V. Assumes V is one of
     *  my vertices. */
    public int inDegree(Vertex v) {
        return v.inDegree();
    }

    /** Returns outDegree(V). This is simply a synonym, intended for
     *  use in undirected graphs. */
    public final int degree(Vertex v) {
        return outDegree(v);
    }

    /** Returns true iff there is an edge (U, V) in me with any label. */
    public boolean contains(Vertex u, Vertex v) {
        return u.containsEdgeTo(v);
    }

    /** Returns true iff there is an edge (U, V) in me with label LABEL. */
    public boolean contains(Vertex u, Vertex v,
                            ELabel label) {
        return u.containsEdgeToWithLabel(v, label);
    }

    /** Returns a new vertex labeled LABEL, and adds it to me with no
     *  incident edges. */
    public Vertex add(VLabel label) {
        Vertex v = new Vertex(label);
        _vertices.add(v);
        return v;
    }

    /** Returns an edge incident on FROM and TO, labeled with LABEL
     *  and adds it to this graph. If I am directed, the edge is directed
     *  (leaves FROM and enters TO). */
    public Edge add(Vertex from,
                    Vertex to,
                    ELabel label) {
        Edge e = new Edge(from, to, label);

        if (isDirected()) {
            from.addOutgoingEdge(e);
            to.addIncomingEdge(e);
        } else {
            from.addOutgoingEdge(e);
            to.addOutgoingEdge(e);
        }

        _edges.add(e);
        return e;
    }

    /** Returns an edge incident on FROM and TO with a null label
     *  and adds it to this graph. If I am directed, the edge is directed
     *  (leaves FROM and enters TO). */
    public Edge add(Vertex from,
                    Vertex to) {
        return add(from, to, null);
    }

    /** Remove V and all adjacent edges, if present. */
    public void remove(Vertex v) {
        Vertex other;
        Set<Edge> elist = new HashSet<Edge>();
        if (isDirected()) {
            for (Edge e : outEdges(v)) {
                elist.add(e);
            }
            for (Edge e : inEdges(v)) {
                elist.add(e);
            }
        } else {
            for (Edge e : edges(v)) {
                elist.add(e);
            }
        }
        for (Edge e : elist) {
            remove(e);
        }
        _vertices.remove(v);
    }

    /** Remove E from me, if present.  E must be between my vertices,
     *  or the result is undefined.  */
    public void remove(Edge e) {
        if (isDirected()) {
            e.getV0().removeOutgoing(e);
            e.getV1().removeIncoming(e);
        } else {
            e.getV0().removeOutgoing(e);
            e.getV1().removeOutgoing(e);
        }
        _edges.remove(e);
    }

    /** Remove all edges from V1 to V2 from me, if present.  The result is
     *  undefined if V1 and V2 are not among my vertices.  */
    public void remove(Vertex v1, Vertex v2) {
        Set<Edge> elist = new HashSet<Edge>();
        for (Edge e : outEdges(v1)) {
            if (e.getV(v1) == v2) {
                elist.add(e);
            }
        }
        for (Edge e : elist) {
            remove(e);
        }
    }

    /** Returns an Iterator over all vertices in arbitrary order. */
    public Iteration<Vertex> vertices() {
        return Iteration.iteration(_vertices);
    }

    /** Returns an iterator over all successors of V. */
    public Iteration<Vertex> successors(Vertex v) {
        return Iteration.iteration(v.successors());
    }

    /** Returns an iterator over all predecessors of V. */
    public Iteration<Vertex> predecessors(Vertex v) {
        return Iteration.iteration(v.predecessors());
    }

    /** Returns successors(V).  This is a synonym typically used on
     *  undirected graphs. */
    public final Iteration<Vertex> neighbors(Vertex v) {
        return successors(v);
    }

    /** Returns an iterator over all edges in me. */
    public Iteration<Edge> edges() {
        return Iteration.iteration(_edges);
    }

    /** Returns iterator over all outgoing edges from V. */
    public Iteration<Edge> outEdges(Vertex v) {
        return Iteration.iteration(v.outgoingEdges());
    }

    /** Returns iterator over all incoming edges to V. */
    public Iteration<Edge> inEdges(Vertex v) {
        return Iteration.iteration(v.incomingEdges());
    }

    /** Returns outEdges(V). This is a synonym typically used
     *  on undirected graphs. */
    public final Iteration<Edge> edges(Vertex v) {
        return outEdges(v);
    }

    /** Cause subsequent calls to edges() to visit or deliver
     *  edges in sorted order, according to COMPARATOR. Subsequent
     *  addition of edges may cause the edges to be reordered
     *  arbitrarily.  */
    public void orderEdges(Comparator<ELabel> comparator) {
        TreeSet<Edge> orderedSet = new TreeSet<Edge>(
                new EdgeComparator(comparator));
        orderedSet.addAll(_edges);
        _edges = orderedSet;
    }

    /** Returns the natural ordering on T, as a Comparator.  For
     *  example, if stringComp = Graph.<Integer>naturalOrder(), then
     *  stringComp.compare(x1, y1) is <0 if x1<y1, ==0 if x1=y1, and >0
     *  otherwise. */
    public static <T extends Comparable<? super T>> Comparator<T> naturalOrder()
    {
        return new Comparator<T>() {
            @Override
            public int compare(T x1, T x2) {
                return x1.compareTo(x2);
            }
        };
    }

    @SuppressWarnings("unchecked")
    /** Compares Vertices. */
    private class VertexComparator implements Comparator<Vertex> {

        @Override
        public int compare(Vertex v0, Vertex v1) {
            VLabel l0 = v0.getLabel();
            VLabel l1 = v1.getLabel();
            if (l0 == null || l1 == null) {
                return 1;
            } else if (l0 instanceof Comparable) {
                int val = ((Comparable<? super VLabel>) l0).compareTo(l1);
                if (val != 0) {
                    return val;
                }
            }

            Integer v0hash = v0.hashCode();
            Integer v1hash = v1.hashCode();
            return Integer.compare(v0hash, v1hash);
        }

    }

    /** Compares Edges, possibly using a Comparator<ELabel>. */
    private class EdgeComparator implements Comparator<Edge> {

        /** Default constructor for EdgeComparator. */
        EdgeComparator() {
            _labelComparator = null;
        }

        /** Alternate constructor for EdgeComparator. Takes in LABELCOMPARATOR 
         *  to compare vertices with. */
        EdgeComparator(Comparator<ELabel> labelComparator) {
            _labelComparator = labelComparator;
        }

        @Override
        public int compare(Edge e0, Edge e1) {
            ELabel l0 = e0.getLabel();
            ELabel l1 = e1.getLabel();
            if (l0 == null || l1 == null) {
                return 1;
            } else if (_labelComparator != null) {
                int val = _labelComparator.compare(l0, l1);
                if (val != 0) {
                    return val;
                }
            }

            Integer e0hash = e0.hashCode();
            Integer e1hash = e1.hashCode();
            return Integer.compare(e0hash, e1hash);
        }

        /** ELabel Comparator. */
        private final Comparator<ELabel> _labelComparator;
    }

    /** Vertices in this graph. */
    private TreeSet<Vertex> _vertices;
    /** Edges in this graph. */
    private TreeSet<Edge> _edges;

}
