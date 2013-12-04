package graph;

import java.util.List;
import java.util.LinkedList;

import java.util.Comparator;

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
                Double l0CrowDist = h.dist(l0, V1.getLabel());
                Double l1CrowDist = h.dist(l1, V1.getLabel());
                Double l0Weight = vweighter.weight(l0);
                Double l1Weight = vweighter.weight(l1);

                return (int) Math.round((l0Weight + l0CrowDist) - 
                        (l1Weight + l1CrowDist));
            }
        };

        try {
            astar.traverse(G, V0, weightOrder);
        } catch (StopException e) {
            return path;
        }

        return null;
    }

    private static class AStarTraversalGeneral<VLabel, ELabel>
            extends Traversal<VLabel, ELabel> {
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
        }

        @Override
        protected void preVisit(Graph<VLabel, ELabel>.Edge e,
                                Graph<VLabel, ELabel>.Vertex v) {
            ELabel elabel = e.getLabel();
            VLabel vlabel = v.getLabel();
            VLabel lastLabel = this.finalVertex().getLabel();

            double vweight = _vweighter.weight(vlabel);
            double lastWeight = _vweighter.weight(lastLabel);
            double edgeWeight = _eweighter.weight(elabel);

            if (vweight > (edgeWeight + lastWeight)) {
                _vweighter.setWeight(vlabel, edgeWeight + lastWeight);

                Graph<VLabel, ELabel>.Edge lastEdge = this.finalEdge();
                _linkMap.put(e, lastEdge);
            }
        }

        @Override
        protected void visit(Graph<VLabel, ELabel>.Vertex v) {
            if (v == _endVertex) {
                Graph<VLabel, ELabel>.Edge lastEdge = this.finalEdge();
                while (lastEdge != null) {
                    _shortestList.addFirst(lastEdge);
                    lastEdge = _linkMap.get(lastEdge);
                }
                throw new StopException();
            }
        }

        private final Map<Graph<VLabel, ELabel>.Edge,
                Graph<VLabel, ELabel>.Edge> _linkMap;
        private final Graph<VLabel, ELabel>.Vertex _endVertex;
        private final Distancer<? super VLabel> _h;
        private final Weighter<? super VLabel> _vweighter;
        private final Weighting<? super ELabel> _eweighter;
        private final LinkedList<Graph<VLabel, ELabel>.Edge> _shortestList;
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
        Traversal<VLabel, ELabel> astar =
            new AStarTraversalWeightableLabels<VLabel, ELabel>(V1, h, path);
                
        Comparator<VLabel> weightOrder = new Comparator<VLabel>() {
            @Override
            public int compare(VLabel l0, VLabel l1) {
                Double l0CrowDist = h.dist(l0, V1.getLabel());
                Double l1CrowDist = h.dist(l1, V1.getLabel());
                Double l0Weight = l0.weight();
                Double l1Weight = l1.weight();

                return (int) Math.round((l0Weight + l0CrowDist) - 
                        (l1Weight + l1CrowDist));
            }
        };

        try {
            astar.traverse(G, V0, weightOrder);
        } catch (StopException e) {
            return path;
        }

        return null;
    }

    private static class 
    AStarTraversalWeightableLabels
    <VLabel extends Weightable, ELabel extends Weighted>
    extends Traversal<VLabel, ELabel> {
        AStarTraversalWeightableLabels(Graph<VLabel, ELabel>.Vertex endVertex,
                Distancer<? super VLabel> h,
                LinkedList<Graph<VLabel, ELabel>.Edge> shortestList) {
            _endVertex = endVertex;
            _h = h;
            _shortestList = shortestList;
            _linkMap = new HashMap<Graph<VLabel, ELabel>.Edge,
                     Graph<VLabel, ELabel>.Edge>();
        }

        @Override
        protected void preVisit(Graph<VLabel, ELabel>.Edge e,
                                Graph<VLabel, ELabel>.Vertex v) {
            ELabel elabel = e.getLabel();
            VLabel vlabel = v.getLabel();
            VLabel lastLabel = this.finalVertex().getLabel();

            double vweight = vlabel.weight();
            double lastWeight = lastLabel.weight();
            double edgeWeight = elabel.weight();

            if (vweight > (edgeWeight + lastWeight)) {
                vlabel.setWeight(edgeWeight + lastWeight);

                Graph<VLabel, ELabel>.Edge lastEdge = this.finalEdge();
                _linkMap.put(e, lastEdge);
            }
        }

        @Override
        protected void visit(Graph<VLabel, ELabel>.Vertex v) {
            if (v == _endVertex) {
                Graph<VLabel, ELabel>.Edge lastEdge = this.finalEdge();
                while (lastEdge != null) {
                    _shortestList.addFirst(lastEdge);
                    lastEdge = _linkMap.get(lastEdge);
                }
                throw new StopException();
            }
        }

        private final Map<Graph<VLabel, ELabel>.Edge,
                Graph<VLabel, ELabel>.Edge> _linkMap;
        private final Graph<VLabel, ELabel>.Vertex _endVertex;
        private final Distancer<? super VLabel> _h;
        private final LinkedList<Graph<VLabel, ELabel>.Edge> _shortestList;
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
