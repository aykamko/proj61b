package graph;

import java.util.Comparator;

import java.util.List;
import java.util.LinkedList;

import java.util.Map;
import java.util.HashMap;

/** Assorted graph algorithms.
 *  @author Aleks Kamko
 */
public final class Graphs {

    /* A* Search Algorithms */

    /** Returns a path from V0 to V1 in G of minimum weight, according
     *  to the edge weighter EWEIGHTER.  VLABEL and ELABEL are the types of
     *  vertex and edge labels.  Assumes that H is a distance measure
     *  between vertices satisfying the two properties:
     *     a. H.dist(v, V1) <= shortest path from v to V1 for any v, and
     *     b. H.dist(v, w) <= H.dist(w, V1) + weight of edge (v, w), where
     *        v and w are any vertices in G.
     *
     *  As a side effect, uses VWEIGHTER to set the weight of vertex v
     *  to the weight of a minimal path from V0 to v, for each v in
     *  the returned path and for each v such that
     *       minimum path length from V0 to v + H.dist(v, V1)
     *              < minimum path length from V0 to V1.
     *  The final weights of other vertices are not defined.  If V1 is
     *  unreachable from V0, returns null and sets the minimum path weights of
     *  all reachable nodes.  The distance to a node unreachable from V0 is
     *  Double.POSITIVE_INFINITY. */
    public static <VLabel, ELabel> List<Graph<VLabel, ELabel>.Edge>
    shortestPath(Graph<VLabel, ELabel> G,
                 Graph<VLabel, ELabel>.Vertex V0,
                 final Graph<VLabel, ELabel>.Vertex V1,
                 final Distancer<? super VLabel> h,
                 final Weighter<? super VLabel> vweighter,
                 Weighting<? super ELabel> eweighter) {

        for (Graph<VLabel, ELabel>.Vertex v : G.vertices()) {
            vweighter.setWeight(v.getLabel(), Double.POSITIVE_INFINITY);
        }
        vweighter.setWeight(V0.getLabel(), 0);

        LinkedList<Graph<VLabel, ELabel>.Edge> path =
            new LinkedList<Graph<VLabel, ELabel>.Edge>();
        Traversal<VLabel, ELabel> astar =
            new AStarTraversalGeneral<VLabel, ELabel>(
                    V1, h, vweighter, eweighter, path);

        Comparator<VLabel> weightOrder = new Comparator<VLabel>() {
            @Override
            public int compare(VLabel l0, VLabel l1) {
                Double l0Weight = h.dist(l0, V1.getLabel())
                    + vweighter.weight(l0);
                Double l1Weight = h.dist(l1, V1.getLabel())
                    + vweighter.weight(l1);

                return Double.compare(l0Weight, l1Weight);
            }
        };

        astar.traverse(G, V0, weightOrder);
        if (path.isEmpty()) {
            return null;
        }

        return path;
    }

    /** Returns a path from V0 to V1 in G of minimum weight, according
     *  to the weights of its edge labels.  VLABEL and ELABEL are the types of
     *  vertex and edge labels.  Assumes that H is a distance measure
     *  between vertices satisfying the two properties:
     *     a. H.dist(v, V1) <= shortest path from v to V1 for any v, and
     *     b. H.dist(v, w) <= H.dist(w, V1) + weight of edge (v, w), where
     *        v and w are any vertices in G.
     *
     *  As a side effect, sets the weight of vertex v to the weight of
     *  a minimal path from V0 to v, for each v in the returned path
     *  and for each v such that
     *       minimum path length from V0 to v + H.dist(v, V1)
     *           < minimum path length from V0 to V1.
     *  The final weights of other vertices are not defined.
     *
     *  This function has the same effect as the 6-argument version of
     *  shortestPath, but uses the .weight and .setWeight methods of
     *  the edges and vertices themselves to determine and set
     *  weights. If V1 is unreachable from V0, returns null and sets
     *  the minimum path weights of all reachable nodes.  The distance
     *  to a node unreachable from V0 is Double.POSITIVE_INFINITY. */
    public static
    <VLabel extends Weightable, ELabel extends Weighted>
    List<Graph<VLabel, ELabel>.Edge>
    shortestPath(Graph<VLabel, ELabel> G,
                 Graph<VLabel, ELabel>.Vertex V0,
                 final Graph<VLabel, ELabel>.Vertex V1,
                 final Distancer<? super VLabel> h) {

        for (Graph<VLabel, ELabel>.Vertex v : G.vertices()) {
            v.getLabel().setWeight(Double.POSITIVE_INFINITY);
        }
        V0.getLabel().setWeight(0);

        LinkedList<Graph<VLabel, ELabel>.Edge> path =
            new LinkedList<Graph<VLabel, ELabel>.Edge>();
        Traversal<VLabel, ELabel> astarweighted =
            new AStarTraversalWeightableLabels<VLabel, ELabel>(V1, h, path);

        Comparator<VLabel> weightOrder = new Comparator<VLabel>() {
            @Override
            public int compare(VLabel l0, VLabel l1) {
                Double l0Weight = h.dist(l0, V1.getLabel())
                    + l0.weight();
                Double l1Weight = h.dist(l1, V1.getLabel())
                    + l1.weight();

                return Double.compare(l0Weight, l1Weight);
            }
        };

        astarweighted.traverse(G, V0, weightOrder);
        if (path.isEmpty()) {
            return null;
        }

        return path;
    }

    /** Subclass of Traversal that uses the A* algorithm as its general
     *  traversal. Uses a Weighter and Weighting to set and read weights
     *  for Vertices and Edges. */
    private static class AStarTraversalGeneral<VLabel, ELabel>
            extends Traversal<VLabel, ELabel> {

        /** Constructor for a Traversal that uses A* as its general traversal.
         *  ENDVERTEX is the vertex to end the traversal at. H is a distancer
         *  that returns the distance between two Vertices based on their
         *  VLabels. VWEIGHTER and EWEIGHTER set and read weights for
         *  Vertices and Edges. SHORTESTLIST becomes the shortest path from
         *  some starting vertex to ENDVERTEX, if there is one; otherwise it
         *  is null. */
        AStarTraversalGeneral(Graph<VLabel, ELabel>.Vertex endVertex,
                Distancer<? super VLabel> h,
                Weighter<? super VLabel> vweighter,
                Weighting<? super ELabel> eweighter,
                LinkedList<Graph<VLabel, ELabel>.Edge> shortestList) {
            _endVertex = endVertex;
            _h = h;
            _vweighter = vweighter;
            _eweighter = eweighter;
            _shortestList = shortestList;
            _linkMap = new HashMap<Graph<VLabel, ELabel>.Edge,
                     Graph<VLabel, ELabel>.Edge>();
            _lastEdgeMap = new HashMap<Graph<VLabel, ELabel>.Vertex,
                            Graph<VLabel, ELabel>.Edge>();
        }

        @Override
        protected void preVisit(Graph<VLabel, ELabel>.Edge e,
                                Graph<VLabel, ELabel>.Vertex v) {
            ELabel elabel = e.getLabel();
            VLabel vlabel = v.getLabel();
            VLabel lastLabel = _lastVertex.getLabel();

            double vweight = getVLabelWeight(vlabel);
            double lastWeight = getVLabelWeight(lastLabel);
            double edgeWeight = getELabelWeight(elabel);

            if (vweight > (edgeWeight + lastWeight)) {
                setVLabelWeight(vlabel, edgeWeight + lastWeight);

                Graph<VLabel, ELabel>.Edge lastEdge =
                    _lastEdgeMap.get(_lastVertex);
                _linkMap.put(e, lastEdge);
                _lastEdgeMap.put(v, e);
            } else {
                throw new RejectException();
            }
        }

        @Override
        protected void visit(Graph<VLabel, ELabel>.Vertex v) {
            if (v == _endVertex) {
                Graph<VLabel, ELabel>.Edge lastEdge = _lastEdgeMap.get(v);
                while (lastEdge != null) {
                    _shortestList.addFirst(lastEdge);
                    lastEdge = _linkMap.get(lastEdge);
                }
                throw new StopException();
            } else {
                _lastVertex = v;
            }
        }

        /** Returns a Double that represents the weight of VLabel V. */
        protected Double getVLabelWeight(VLabel v) {
            return _vweighter.weight(v);
        }

        /** Sets weight of VLabel V to W. */
        protected void setVLabelWeight(VLabel v, double w) {
            _vweighter.setWeight(v, w);
        }

        /** Returns a Double that represents the weight of ELabel E. */
        protected Double getELabelWeight(ELabel e) {
            return _eweighter.weight(e);
        }

        /** Map that represents a linked tree. Each key is an Edge, each value
         *  is an Edge that is the 'parent' of the key Edge in a shortest path
         *  from the initial Edge to the key Edge. */
        protected final Map<Graph<VLabel, ELabel>.Edge,
                Graph<VLabel, ELabel>.Edge> _linkMap;
        /** Vertex to end traversal at. */
        protected final Graph<VLabel, ELabel>.Vertex _endVertex;
        /** Computes crow distance between two vertices. */
        protected final Distancer<? super VLabel> _h;
        /** Set and reads weights for VLabels. */
        protected final Weighter<? super VLabel> _vweighter;
        /** Reads weights of ELabels. */
        protected final Weighting<? super ELabel> _eweighter;

        /** Last visited vertex. */
        protected Graph<VLabel, ELabel>.Vertex _lastVertex;
        /** Map of last traversed edges. */
        protected Map<Graph<VLabel, ELabel>.Vertex,
                Graph<VLabel, ELabel>.Edge> _lastEdgeMap;
        /** Shortest path from the starting vertex of a traversal to
         *  _endVertex, if it exists. null otherwise. */
        protected final LinkedList<Graph<VLabel, ELabel>.Edge> _shortestList;
    }


    /** Subclass of AStarGeneralTraversal that necessitates that Vertices and
     *  Edges extend Weightable and Weighted, respectively. Weights are set and
     *  read using inherited methods from the previously mentioned classes. */
    private static class
    AStarTraversalWeightableLabels
        <VLabel extends Weightable, ELabel extends Weighted>
        extends AStarTraversalGeneral<VLabel, ELabel> {

        /** Constructor for a Traversal that uses A* as its general traversal.
         *  Vertices and Edges are assumed to extend Weightable and Weighted,
         *  respectively. ENDVERTEX is the vertex to end the traversal at. H
         *  is a dlstancer that returns the distance between two Vertices based
         *  on their VLabels. SHORTESTLIST becomes the shortest path from some
         *  starting vertex to ENDVERTEX, if there is one; otherwise it is
         *  null. */
        AStarTraversalWeightableLabels(Graph<VLabel, ELabel>.Vertex endVertex,
                Distancer<? super VLabel> h,
                LinkedList<Graph<VLabel, ELabel>.Edge> shortestList) {
            super(endVertex, h, null, null, shortestList);
        }

        @Override
        protected Double getVLabelWeight(VLabel v) {
            return v.weight();
        }

        @Override
        protected void setVLabelWeight(VLabel v, double w) {
            v.setWeight(w);
        }

        @Override
        protected Double getELabelWeight(ELabel e) {
            return e.weight();
        }
    }

    /** Returns a distancer whose dist method always returns 0. */
    public static final Distancer<Object> ZERO_DISTANCER =
        new Distancer<Object>() {
            @Override
            public double dist(Object v0, Object v1) {
                return 0.0;
            }
        };
}
