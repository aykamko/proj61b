package graph;

import java.util.Comparator;

import java.util.Collections;

import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import java.util.Set;
import java.util.HashSet;

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
            _outgoing = new ArrayList<Edge>();
            _incoming = new ArrayList<Edge>();
        }

        /** Returns the label on this vertex. */
        public VLabel getLabel() {
            return _label;
        }

        /** Adds an outgoing edge from this vertex. */
        private void addOutgoingEdge(Edge edge) {
            _outgoing.add(edge);
        }

        /** Adds an incoming edge from this vertex. */
        private void addIncomingEdge(Edge edge) {
            _incoming.add(edge);
        }

        /** Adds an edge to this vertex. (In an undirected graph.) */
        private void addEdge(Edge edge) {
            _outgoing.add(edge);
        }

        /** Removes outgoing edge EDGE from this vertex. */
        private void removeOutgoing(Edge edge) {
            _outgoing.remove(edge);
        }

        /** Removes incoming edge EDGE from this vertex. */
        private void removeIncoming(Edge edge) {
            _incoming.remove(edge);
        }

        /** Removes edge EDGE from this vertex. (Convenience method for
         *  undirected graphs.) */
        private void removeEdge(Edge edge) {
            _outgoing.remove(edge);
        }

        /** Returns an iterator over the outgoing edges of this vertex. */
        private Iterator<Edge> outgoingEdges() {
            return _outgoing.iterator();
        }

        /** Returns an iterator over the incoming edges of this vertex. */
        private Iterator<Edge> incomingEdges() {
            return _incoming.iterator();
        }

        /** Returns the number of outgoing edges from this vertex. */
        private int outDegree() {
            return _outgoing.size();
        }

        /** Returns the number of incoming edges into this vertex. */
        private int inDegree() {
            return _incoming.size();
        }

        /** Returns the number of edges adjacent this vertex. */
        private int degree() {
            return _outgoing.size() + _incoming.size();
        }

        /** Returns true iff this Vertex contains an outgoing edge
         *  with V as its opposite vertex. */
        private boolean containsEdgeTo(Vertex v) {
            for (Edge e : _outgoing) {
                if (e.getV(this) == v) {
                    return true;
                }
            }
            return false;
        }

        /** Returns true iff this Vertex contains an outgoing edge
         *  with label LABEL with V as its opposite vertex. */
        private boolean containsEdgeToWithLabel(Vertex v, ELabel label) {
            for (Edge e : _outgoing) {
                if (e.getV(this) == v && e.getLabel().equals(label)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return String.valueOf(_label);
        }

        /** The label on this vertex. */
        private final VLabel _label;
        /** Outgoing edges of this vertex. */
        private final List<Edge> _outgoing;
        /** Incoming edges of this vertex. */
        private final List<Edge> _incoming;
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
        _edgeLabels = new ArrayList<ELabel>();
        _vertexLabels = new ArrayList<VLabel>();
        _edges = new HashMap<ELabel, LinkedList<Edge>>();
        _vertices = new HashMap<VLabel, LinkedList<Vertex>>();
        _vertNum = 0;
        _edgeNum = 0;
    }

    /** Returns the number of vertices in me. */
    public int vertexSize() {
        return _vertNum;
    }

    /** Returns the number of edges in me. */
    public int edgeSize() {
        return _edgeNum;
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
        LinkedList<Vertex> vlist = _vertices.get(label);
        if (vlist == null) {
            vlist = new LinkedList<Vertex>();
            _vertices.put(label, vlist);
        }
        Vertex v = new Vertex(label);
        _vertexLabels.add(label);
        vlist.add(v);
        _vertNum += 1;
        return v;
    }

    /** Returns an edge incident on FROM and TO, labeled with LABEL
     *  and adds it to this graph. If I am directed, the edge is directed
     *  (leaves FROM and enters TO). */
    public Edge add(Vertex from,
                    Vertex to,
                    ELabel label) {
        LinkedList<Edge> elist = _edges.get(label);
        if (elist == null) {
            elist = new LinkedList<Edge>();
            _edges.put(label, elist);
        }
        Edge e = new Edge(from, to, label);
        _edgeLabels.add(label);
        elist.add(e);

        if (isDirected()) {
            from.addOutgoingEdge(e);
            to.addIncomingEdge(e);
        } else {
            from.addEdge(e);
            to.addEdge(e);
        }

        _edgeNum += 1;
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
        _vertexLabels.remove(v.getLabel());
        LinkedList<Vertex> vlist = _vertices.get(v.getLabel());
        _vertNum -= 1;
        vlist.remove(v);
    }

    /** Remove E from me, if present.  E must be between my vertices,
     *  or the result is undefined.  */
    public void remove(Edge e) {
        if (isDirected()) {
            e.getV0().removeOutgoing(e);
            e.getV1().removeIncoming(e);
        } else {
            e.getV0().removeEdge(e);
            e.getV1().removeEdge(e);
        }
        _edgeLabels.remove(e.getLabel());
        LinkedList<Edge> elist = _edges.get(e.getLabel());
        _edgeNum -= 1;
        elist.remove(e);
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
        return Iteration.iteration(
                new ValueIterator<VLabel, Vertex>(_vertexLabels, _vertices));
    }

    /** Returns an iterator over all successors of V. */
    public Iteration<Vertex> successors(Vertex v) {
        return Iteration.iteration(
                new OtherVertexIterator(outEdges(v), v));
    }

    /** Returns an iterator over all predecessors of V. */
    public Iteration<Vertex> predecessors(Vertex v) {
        return Iteration.iteration(
                new OtherVertexIterator(inEdges(v), v));
    }


    /** Returns successors(V).  This is a synonym typically used on
     *  undirected graphs. */
    public final Iteration<Vertex> neighbors(Vertex v) {
        return successors(v);
    }

    /** Returns an iterator over all edges in me. */
    public Iteration<Edge> edges() {
        return Iteration.iteration(
                new ValueIterator<ELabel, Edge>(_edgeLabels, _edges));
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
        Collections.sort(_edgeLabels, comparator);
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

    /** Iterates over the values in VALUEMAP using the order of the keys
     *  in KEYLIST. */
    private static class ValueIterator<Key, Value> implements Iterator<Value> {
        ValueIterator(List<Key> keyList,
                Map<Key, LinkedList<Value>> valueMap) {
            _keyList = keyList;
            _valueMap = valueMap;
            _keyIter = _keyList.iterator();
            _valueIter = null;
            _lastKey = null;
        }

        @Override
        public boolean hasNext() {
            if (_valueIter == null || !_valueIter.hasNext()) {
                return _keyIter.hasNext();
            }
            return true;
        }

        @Override
        public Value next() {
            if ((_valueIter == null || !_valueIter.hasNext())
                    && _keyIter.hasNext()) {
                Key newKey = null;
                do {
                    newKey = _keyIter.next();
                } while (_lastKey != null && _lastKey.equals(newKey));
                _lastKey = newKey;
                _valueIter = _valueMap.get(_lastKey).iterator();
            }
            return _valueIter.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove not supported");
        }

        private Key _lastKey;
        private Iterator<Value> _valueIter;
        private final Iterator<Key> _keyIter;
        private final List<Key> _keyList;
        private final Map<Key, LinkedList<Value>> _valueMap;
    }


    /** Class that iterates over the vertices on the opposite side
     *  of the edges returned by iteration ITER from some adjacent vertex V. */
    private class OtherVertexIterator implements Iterator<Vertex> {
        OtherVertexIterator(Iteration<Edge> iter, Vertex v) {
            _iter = iter;
            _v = v;
        }

        @Override
        public boolean hasNext() {
            return _iter.hasNext();
        }

        @Override
        public Vertex next() {
            return _iter.next().getV(_v);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove not supported");
        }

        private final Vertex _v;
        private final Iteration<Edge> _iter;
    }

    /** Labels for edges in a graph. */
    private List<ELabel> _edgeLabels;
    /** Labels for vertices in a graph. */
    private List<VLabel> _vertexLabels;

    /** Edges in this graph. */
    private Map<ELabel, LinkedList<Edge>> _edges;
    /** Vertices in this graph. */
    private Map<VLabel, LinkedList<Vertex>> _vertices;

    /** Number of vertices in this graph. */
    private int _vertNum;
    /** Number of edges in this graph. */
    private int _edgeNum;

}
