package graph;

import java.util.Comparator;

import java.util.Set;
import java.util.TreeSet;

import java.util.List;
import java.util.ArrayList;


/* Do not add or remove public or protected members, or modify the signatures of
 * any public methods.  You may make changes that don't affect the API as seen
 * from outside the graph package:
 *   + You may make methods in Graph abstract, if you want different
 *     implementations in DirectedGraph and UndirectedGraph.
 *   + You may add bodies to abstract methods, modify existing bodies,
 *     or override inherited methods.
 *   + You may change parameter names, or add 'final' modifiers to parameters.
 *   + You may private and package private members.
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
            _edges = _outgoing;
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

        @Override
        public String toString() {
            return String.valueOf(_label);
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object obj) {
            if (obj instanceof Graph.Vertex) {
                Vertex compare = (Vertex) obj;
                return this.getLabel().equals(compare.getLabel());
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return this.getLabel().hashCode();
        }

        /** The label on this vertex. */
        private final VLabel _label;
        /** Outgoing edges of this vertex. */
        private final List<Edge> _outgoing;
        /** Incoming edges of this vertex. */
        private final List<Edge> _incoming;
        /** Edges of this vertex (in an undirected graph). This is just
         *  for convenience. */
        private final List<Edge> _edges;

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

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object obj) {
            if (obj instanceof Graph.Edge) {
                Edge compare = (Edge) obj;
                return this.getV0().equals(compare.getV0())
                    && this.getV1().equals(compare.getV1())
                    && this.getLabel().equals(compare.getLabel());
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return this.getV0().hashCode()
                 + (HASH_CONST * this.getV1().hashCode())
                 + ((int)Math.pow(HASH_CONST, 2) * this.getLabel().hashCode());
        }

        /** Endpoints of this edge.  In directed edges, this edge exits _V0
         *  and enters _V1. */
        private final Vertex _v0, _v1;

        /** The label on this edge. */
        private final ELabel _label;

    }

    /*=====  Methods and variables of Graph =====*/

    /** Constructs and empty graph. */
    Graph() {
        _edges = new TreeSet<Edge>();
        _vertices = new TreeSet<Vertex>();
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
        return _edges.contains(new Edge(u, v, null));
    }

    /** Returns true iff there is an edge (U, V) in me with label LABEL. */
    public boolean contains(Vertex u, Vertex v,
                            ELabel label) {
        // FIXME
        return false;
    }

    /** Returns a new vertex labeled LABEL, and adds it to me with no
     *  incident edges. */
    public Vertex add(VLabel label) {
        // FIXME
        return null;
    }

    /** Returns an edge incident on FROM and TO, labeled with LABEL
     *  and adds it to this graph. If I am directed, the edge is directed
     *  (leaves FROM and enters TO). */
    public Edge add(Vertex from,
                    Vertex to,
                    ELabel label) {
        // FIXME
        return null;
    }

    /** Returns an edge incident on FROM and TO with a null label
     *  and adds it to this graph. If I am directed, the edge is directed
     *  (leaves FROM and enters TO). */
    public Edge add(Vertex from,
                    Vertex to) {
        // FIXME
        return null;
    }

    /** Remove V and all adjacent edges, if present. */
    public void remove(Vertex v) {
        // FIXME
    }

    /** Remove E from me, if present.  E must be between my vertices,
     *  or the result is undefined.  */
    public void remove(Edge e) {
        // FIXME
    }

    /** Remove all edges from V1 to V2 from me, if present.  The result is
     *  undefined if V1 and V2 are not among my vertices.  */
    public void remove(Vertex v1, Vertex v2) {
        // FIXME
    }

    /** Returns an Iterator over all vertices in arbitrary order. */
    public Iteration<Vertex> vertices() {
        // FIXME
        return null;
    }

    /** Returns an iterator over all successors of V. */
    public Iteration<Vertex> successors(Vertex v) {
        // FIXME
        return null;
    }

    /** Returns an iterator over all predecessors of V. */
    public Iteration<Vertex> predecessors(Vertex v) {
        return null;
        // FIXME
    }

    /** Returns successors(V).  This is a synonym typically used on
     *  undirected graphs. */
    public final Iteration<Vertex> neighbors(Vertex v) {
        return successors(v);
    }

    /** Returns an iterator over all edges in me. */
    public Iteration<Edge> edges() {
        return null;
        // FIXME
    }

    /** Returns iterator over all outgoing edges from V. */
    public Iteration<Edge> outEdges(Vertex v) {
        return null;
        // FIXME
    }

    /** Returns iterator over all incoming edges to V. */
    public Iteration<Edge> inEdges(Vertex v) {
        return null;
        // FIXME
    }

    /** Returns outEdges(V). This is a synonym typically used
     *  on undirected graphs. */
    public final Iteration<Edge> edges(Vertex v) {
        return outEdges(v);
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

    /** Cause subsequent calls to edges() to visit or deliver
     *  edges in sorted order, according to COMPARATOR. Subsequent
     *  addition of edges may cause the edges to be reordered
     *  arbitrarily.  */
    public void orderEdges(Comparator<ELabel> comparator) {
        // FIXME
    }

    /** Edges in this graph. */
    protected final TreeSet _edges;
    /** Vertices in this graph. */
    protected final TreeSet _vertices;

}
