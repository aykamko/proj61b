package graph;

import java.util.List;
import java.util.LinkedList;

import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;

import java.util.Map;
import java.util.HashMap;

import java.util.Comparator;

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
                 Graph<VLabel, ELabel>.Vertex V1,
                 Distancer<? super VLabel> h,
                 Weighter<? super VLabel> vweighter,
                 Weighting<? super ELabel> eweighter) {
        Set<Graph<VLabel, ELabel>.Vertex> closedSet =
            new HashSet<Graph<VLabel, ELabel>.Vertex>();
        TreeSet<Graph<VLabel, ELabel>.Vertex> openSet =
            new TreeSet<Graph<VLabel, ELabel>.Vertex>
            (new GenericVertexWeightOrderComparator<VLabel, ELabel>
            (h, V1, vweighter));
        Map<Graph<VLabel, ELabel>.Vertex,
            Graph<VLabel, ELabel>.Edge> vertEdgeMap =
            new HashMap<Graph<VLabel, ELabel>.Vertex,
                Graph<VLabel, ELabel>.Edge>();

        for (Graph<VLabel, ELabel>.Vertex v : G.vertices()) {
            vweighter.setWeight(v.getLabel(), Double.POSITIVE_INFINITY);
        }
        vweighter.setWeight(V0.getLabel(), 0);
        openSet.add(V0);

        Graph<VLabel, ELabel>.Vertex v, u;
        Double newScore, curScore;
        while (!openSet.isEmpty()) {
            v = openSet.pollFirst();
            if (v == V1) {
                return reconstructPath(vertEdgeMap, v);
            }

            closedSet.add(v);
            for (Graph<VLabel, ELabel>.Edge e : G.outEdges(v)) {
                u = e.getV(v);
                newScore = vweighter.weight(v.getLabel())
                    + eweighter.weight(e.getLabel());
                curScore = vweighter.weight(u.getLabel());
                if (closedSet.contains(u)
                        && Double.compare(newScore, curScore) >= 0) {
                    continue;
                }

                if (!openSet.contains(u)
                        || Double.compare(newScore, curScore) < 0) {
                    vertEdgeMap.put(u, e);
                    openSet.remove(u);
                    vweighter.setWeight(u.getLabel(), newScore);
                    openSet.add(u);
                }
            }
        }

        return null;
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
                 Graph<VLabel, ELabel>.Vertex V1,
                 Distancer<? super VLabel> h) {
        Set<Graph<VLabel, ELabel>.Vertex> closedSet =
            new HashSet<Graph<VLabel, ELabel>.Vertex>();
        TreeSet<Graph<VLabel, ELabel>.Vertex> openSet =
            new TreeSet<Graph<VLabel, ELabel>.Vertex>
            (new WeightableVertexWeightOrderComparator<VLabel, ELabel>(h, V1));
        Map<Graph<VLabel, ELabel>.Vertex,
            Graph<VLabel, ELabel>.Edge> vertEdgeMap =
            new HashMap<Graph<VLabel, ELabel>.Vertex,
                Graph<VLabel, ELabel>.Edge>();

        for (Graph<VLabel, ELabel>.Vertex v : G.vertices()) {
            v.getLabel().setWeight(Double.POSITIVE_INFINITY);
        }
        V0.getLabel().setWeight(0);
        openSet.add(V0);

        Graph<VLabel, ELabel>.Vertex v, u;
        Double newScore, curScore;
        while (!openSet.isEmpty()) {
            v = openSet.pollFirst();
            if (v == V1) {
                return reconstructPath(vertEdgeMap, v);
            }

            closedSet.add(v);
            for (Graph<VLabel, ELabel>.Edge e : G.outEdges(v)) {
                u = e.getV(v);
                newScore = v.getLabel().weight() + e.getLabel().weight();
                curScore = u.getLabel().weight();
                if (closedSet.contains(u)
                        && Double.compare(newScore, curScore) >= 0) {
                    continue;
                }

                if (!openSet.contains(u)
                        || Double.compare(newScore, curScore) < 0) {
                    vertEdgeMap.put(u, e);
                    openSet.remove(u);
                    u.getLabel().setWeight(newScore);
                    openSet.add(u);
                }
            }
        }

        return null;
    }

    /** Reconstructs the path from LASTVERTEX to the beginning of an A*
     *  traversal, as defined by VERTEDGEMAP. Returns the path in a List.
     *  VLABEL is the type of vertex labels, and ELABEL is the type of
     *  edge labels. */
    private static <VLabel, ELabel> List<Graph<VLabel, ELabel>.Edge>
    reconstructPath(
            Map<Graph<VLabel, ELabel>.Vertex,
                Graph<VLabel, ELabel>.Edge> vertEdgeMap,
            Graph<VLabel, ELabel>.Vertex lastVertex) {
        if (lastVertex == null) {
            return null;
        }

        Graph<VLabel, ELabel>.Edge edge = vertEdgeMap.get(lastVertex);
        LinkedList<Graph<VLabel, ELabel>.Edge> path =
            new LinkedList<Graph<VLabel, ELabel>.Edge>();
        while (edge != null) {
            path.addFirst(edge);
            lastVertex = edge.getV(lastVertex);
            edge = vertEdgeMap.get(lastVertex);
        }

        return path;
    }

    /** Comparator that order Vertices by weight order. */
    private static class GenericVertexWeightOrderComparator<VLabel, ELabel>
        implements Comparator<Graph<VLabel, ELabel>.Vertex> {

        /** Default constructor. Takes in a Distancer H, an ending
         *  vertex END, and a VLabel Weighter VWEIGHTER. */
        GenericVertexWeightOrderComparator(Distancer<? super VLabel> h,
            Graph<VLabel, ELabel>.Vertex end,
            Weighter<? super VLabel> vweighter) {
            _h = h;
            _end = end;
            _vweighter = vweighter;
        }

        @Override
        public int compare(Graph<VLabel, ELabel>.Vertex v0,
                Graph<VLabel, ELabel>.Vertex v1) {
            Double v0Weight = getVertexWeight(v0)
                + _h.dist(v0.getLabel(), _end.getLabel());
            Double v1Weight = getVertexWeight(v1)
                + _h.dist(v1.getLabel(), _end.getLabel());

            return Double.compare(v0Weight, v1Weight);
        }

        /** Returns the weight of Vertex V. */
        protected Double getVertexWeight(Graph<VLabel, ELabel>.Vertex v) {
            return _vweighter.weight(v.getLabel());
        }

        /** Ending vertex. Used to measure distance. */
        private final Graph<VLabel, ELabel>.Vertex _end;
        /** Measures distances between vertices. */
        private final Distancer<? super VLabel> _h;
        /** Weights vertices. */
        private final Weighter<? super VLabel> _vweighter;
    }

    /** Comparator that orders Vertices by weight order. Vertices must
     *  implement Weightable. */
    private static class WeightableVertexWeightOrderComparator
        <VLabel extends Weightable, ELabel extends Weighted>
        extends GenericVertexWeightOrderComparator<VLabel, ELabel> {

        /** Default constructor. Takes in a Distancer H and an ending
         *  vertex END. */
        WeightableVertexWeightOrderComparator(Distancer<? super VLabel> h,
            Graph<VLabel, ELabel>.Vertex end) {
            super(h, end, null);
        }

        @Override
        protected Double getVertexWeight(Graph<VLabel, ELabel>.Vertex v) {
            return v.getLabel().weight();
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
