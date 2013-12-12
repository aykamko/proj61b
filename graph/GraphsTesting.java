package graph;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

import java.util.Map;
import java.util.HashMap;

import java.util.Arrays;

/** Unit tests for the Graphs.java (A* Traversal).
 *  @author Aleks Kamko
 */
public class GraphsTesting {

    @Test
    public void shortestPathUndirected() {
        UndirectedGraph<WeightablePlace, WeightedPath> g =
            new UndirectedGraph<WeightablePlace, WeightedPath>();
        Graph<WeightablePlace, WeightedPath>.Vertex
        vA, vB, vC, vD, vE, vF, vG, vH, vI, vJ, vK;
        vA = g.add(new WeightablePlace("A", 3, 6));
        vB = g.add(new WeightablePlace("B", 5, 2));
        vC = g.add(new WeightablePlace("C", 8, 5));
        vD = g.add(new WeightablePlace("D", 9, 2));
        vE = g.add(new WeightablePlace("E", 10, 4));
        vF = g.add(new WeightablePlace("F", 13, 3));
        vG = g.add(new WeightablePlace("G", 10, 7));
        vH = g.add(new WeightablePlace("H", 5, 11));
        vI = g.add(new WeightablePlace("I", 8, 15));
        vJ = g.add(new WeightablePlace("J", 11, 12));
        vK = g.add(new WeightablePlace("K", 12, 8));
        Graph<WeightablePlace, WeightedPath>.Edge
        eAB, eAC, eBC, eDE, eCG, eAH, eHJ, eGJ, eIJ, eIG, eIF, eGK;
        eAB = addDistancedPath(g, _H, vA, vB, "AB");
        eAC = addDistancedPath(g, _H, vA, vC, "AC");
        eBC = addDistancedPath(g, _H, vB, vC, "BC");
        eDE = addDistancedPath(g, _H, vD, vE, "DE");
        eCG = addDistancedPath(g, _H, vC, vG, "CG");
        eAH = addDistancedPath(g, _H, vA, vH, "AH");
        eHJ = addDistancedPath(g, _H, vH, vJ, "HJ");
        eGJ = addDistancedPath(g, _H, vG, vJ, "GJ");
        eIJ = addDistancedPath(g, _H, vI, vJ, "IJ");
        eIG = addDistancedPath(g, _H, vI, vG, "IG");
        eIF = addDistancedPath(g, _H, vI, vF, "IF");
        eGK = addDistancedPath(g, _H, vG, vK, "GK");

        List<Graph<WeightablePlace, WeightedPath>.Edge> path =
            Graphs.<WeightablePlace, WeightedPath>shortestPath(g, vA, vE, _H);
        assertTrue("incorrect: A*", path == null);

        path = Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, vA, vD, _H);
        assertTrue("incorrect: A*", path == null);

        path = Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, vA, vI, _H);
        List<Graph<WeightablePlace, WeightedPath>.Edge> compare =
            Arrays.asList(eAH, eHJ, eIJ);
        assertArrayEquals("incorrect: A*", compare.toArray(), path.toArray());

        path = Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, vA, vF, _H);
        compare = Arrays.asList(eAH, eHJ, eIJ, eIF);
        assertArrayEquals("incorrect: A*", compare.toArray(), path.toArray());

        path = Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, vA, vK, _H);
        compare = Arrays.asList(eAC, eCG, eGK);
        assertArrayEquals("incorrect: A*", compare.toArray(), path.toArray());
    }

    @Test
    public void shortestPathDirected() {
        DirectedGraph<WeightablePlace, WeightedPath> g =
            new DirectedGraph<WeightablePlace, WeightedPath>();
        Graph<WeightablePlace, WeightedPath>.Vertex
        vA, vB, vC, vD, vE, vF, vG, vH, vI, vJ, vK;
        vA = g.add(new WeightablePlace("A", 3, 6));
        vB = g.add(new WeightablePlace("B", 5, 2));
        vC = g.add(new WeightablePlace("C", 8, 5));
        vD = g.add(new WeightablePlace("D", 9, 2));
        vE = g.add(new WeightablePlace("E", 10, 4));
        vF = g.add(new WeightablePlace("F", 13, 3));
        vG = g.add(new WeightablePlace("G", 10, 7));
        vH = g.add(new WeightablePlace("H", 5, 11));
        vI = g.add(new WeightablePlace("I", 8, 15));
        vJ = g.add(new WeightablePlace("J", 11, 12));
        vK = g.add(new WeightablePlace("K", 12, 8));
        Graph<WeightablePlace, WeightedPath>.Edge
        eAB, eAC, eBC, eDE, eCG, eAH, eHJ, eGJ, eIJ, eGI, eIF, eGK;
        eAB = addDistancedPath(g, _H, vA, vB, "AB");
        eAC = addDistancedPath(g, _H, vA, vC, "AC");
        eBC = addDistancedPath(g, _H, vB, vC, "BC");
        eDE = addDistancedPath(g, _H, vD, vE, "DE");
        eCG = addDistancedPath(g, _H, vC, vG, "CG");
        eAH = addDistancedPath(g, _H, vA, vH, "AH");
        eHJ = addDistancedPath(g, _H, vH, vJ, "HJ");
        eGJ = addDistancedPath(g, _H, vG, vJ, "GJ");
        eIJ = addDistancedPath(g, _H, vI, vJ, "IJ");
        eGI = addDistancedPath(g, _H, vG, vI, "GI");
        eIF = addDistancedPath(g, _H, vI, vF, "IF");
        eGK = addDistancedPath(g, _H, vG, vK, "GK");

        List<Graph<WeightablePlace, WeightedPath>.Edge> path =
            Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, vA, vE, _H);
        assertTrue("incorrect: A*", path == null);

        path = Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, vK, vG, _H);
        assertTrue("incorrect: A*", path == null);

        path = Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, vB, vA, _H);
        assertTrue("incorrect: A*", path == null);

        path = Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, vA, vI, _H);
        List<Graph<WeightablePlace, WeightedPath>.Edge> compare =
            Arrays.asList(eAC, eCG, eGI);
        assertArrayEquals("incorrect: A*", compare.toArray(), path.toArray());

        path = Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, vA, vF, _H);
        compare = Arrays.asList(eAC, eCG, eGI, eIF);
        assertArrayEquals("incorrect: A*", compare.toArray(), path.toArray());

        path = Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, vA, vK, _H);
        compare = Arrays.asList(eAC, eCG, eGK);
        assertArrayEquals("incorrect: A*", compare.toArray(), path.toArray());
    }

    @Test
    public void shortestPathUndirectedNotWeightable() {
        UndirectedGraph<Place, String> g =
            new UndirectedGraph<Place, String>();
        initializeWeightMaps();
        Graph<Place, String>.Vertex
        vA, vB, vC, vD, vE, vF, vG, vH, vI, vJ, vK;
        vA = g.add(new Place("A", 3, 6));
        vB = g.add(new Place("B", 5, 2));
        vC = g.add(new Place("C", 8, 5));
        vD = g.add(new Place("D", 9, 2));
        vE = g.add(new Place("E", 10, 4));
        vF = g.add(new Place("F", 13, 3));
        vG = g.add(new Place("G", 10, 7));
        vH = g.add(new Place("H", 5, 11));
        vI = g.add(new Place("I", 8, 15));
        vJ = g.add(new Place("J", 11, 12));
        vK = g.add(new Place("K", 12, 8));
        Graph<Place, String>.Edge
        eAB, eAC, eBC, eDE, eCG, eAH, eHJ, eGJ, eIJ, eIG, eIF, eGK;
        eAB = addDistancedPathNotWeightable(g, _H, vA, vB, "AB");
        eAC = addDistancedPathNotWeightable(g, _H, vA, vC, "AC");
        eBC = addDistancedPathNotWeightable(g, _H, vB, vC, "BC");
        eDE = addDistancedPathNotWeightable(g, _H, vD, vE, "DE");
        eCG = addDistancedPathNotWeightable(g, _H, vC, vG, "CG");
        eAH = addDistancedPathNotWeightable(g, _H, vA, vH, "AH");
        eHJ = addDistancedPathNotWeightable(g, _H, vH, vJ, "HJ");
        eGJ = addDistancedPathNotWeightable(g, _H, vG, vJ, "GJ");
        eIJ = addDistancedPathNotWeightable(g, _H, vI, vJ, "IJ");
        eIG = addDistancedPathNotWeightable(g, _H, vI, vG, "IG");
        eIF = addDistancedPathNotWeightable(g, _H, vI, vF, "IF");
        eGK = addDistancedPathNotWeightable(g, _H, vG, vK, "GK");
        MapVertexWeighter vw = new MapVertexWeighter();
        MapEdgeWeighting ew = new MapEdgeWeighting();

        List<Graph<Place, String>.Edge> path =
            Graphs.<Place, String>shortestPath(g, vA, vE, _H, vw, ew);
        assertTrue("incorrect: A*", path == null);

        path = Graphs.<Place, String>
            shortestPath(g, vA, vD, _H, vw, ew);
        assertTrue("incorrect: A*", path == null);

        path = Graphs.<Place, String>
            shortestPath(g, vA, vI, _H, vw, ew);
        List<Graph<Place, String>.Edge> compare =
            Arrays.asList(eAH, eHJ, eIJ);
        assertArrayEquals("incorrect: A*", compare.toArray(), path.toArray());

        path = Graphs.<Place, String>
            shortestPath(g, vA, vF, _H, vw, ew);
        compare = Arrays.asList(eAH, eHJ, eIJ, eIF);
        assertArrayEquals("incorrect: A*", compare.toArray(), path.toArray());

        path = Graphs.<Place, String>
            shortestPath(g, vA, vK, _H, vw, ew);
        compare = Arrays.asList(eAC, eCG, eGK);
        assertArrayEquals("incorrect: A*", compare.toArray(), path.toArray());
    }

    @Test
    public void shortestPathDirectedNotWeightable() {
        DirectedGraph<Place, String> g =
            new DirectedGraph<Place, String>();
        initializeWeightMaps();
        Graph<Place, String>.Vertex
        vA, vB, vC, vD, vE, vF, vG, vH, vI, vJ, vK;
        vA = g.add(new Place("A", 3, 6));
        vB = g.add(new Place("B", 5, 2));
        vC = g.add(new Place("C", 8, 5));
        vD = g.add(new Place("D", 9, 2));
        vE = g.add(new Place("E", 10, 4));
        vF = g.add(new Place("F", 13, 3));
        vG = g.add(new Place("G", 10, 7));
        vH = g.add(new Place("H", 5, 11));
        vI = g.add(new Place("I", 8, 15));
        vJ = g.add(new Place("J", 11, 12));
        vK = g.add(new Place("K", 12, 8));
        Graph<Place, String>.Edge
        eAB, eAC, eBC, eDE, eCG, eAH, eHJ, eGJ, eIJ, eGI, eIF, eGK;
        eAB = addDistancedPathNotWeightable(g, _H, vA, vB, "AB");
        eAC = addDistancedPathNotWeightable(g, _H, vA, vC, "AC");
        eBC = addDistancedPathNotWeightable(g, _H, vB, vC, "BC");
        eDE = addDistancedPathNotWeightable(g, _H, vD, vE, "DE");
        eCG = addDistancedPathNotWeightable(g, _H, vC, vG, "CG");
        eAH = addDistancedPathNotWeightable(g, _H, vA, vH, "AH");
        eHJ = addDistancedPathNotWeightable(g, _H, vH, vJ, "HJ");
        eGJ = addDistancedPathNotWeightable(g, _H, vG, vJ, "GJ");
        eIJ = addDistancedPathNotWeightable(g, _H, vI, vJ, "IJ");
        eGI = addDistancedPathNotWeightable(g, _H, vG, vI, "GI");
        eIF = addDistancedPathNotWeightable(g, _H, vI, vF, "IF");
        eGK = addDistancedPathNotWeightable(g, _H, vG, vK, "GK");
        MapVertexWeighter vw = new MapVertexWeighter();
        MapEdgeWeighting ew = new MapEdgeWeighting();

        List<Graph<Place, String>.Edge> path =
            Graphs.<Place, String>
            shortestPath(g, vA, vE, _H, vw, ew);
        assertTrue("incorrect: A*", path == null);
        path = Graphs.<Place, String>
            shortestPath(g, vK, vG, _H, vw, ew);
        assertTrue("incorrect: A*", path == null);
        path = Graphs.<Place, String>
            shortestPath(g, vB, vA, _H, vw, ew);
        assertTrue("incorrect: A*", path == null);

        path = Graphs.<Place, String>
            shortestPath(g, vA, vI, _H, vw, ew);
        List<Graph<Place, String>.Edge> compare =
            Arrays.asList(eAC, eCG, eGI);
        assertArrayEquals("incorrect: A*", compare.toArray(), path.toArray());
        path = Graphs.<Place, String>
            shortestPath(g, vA, vF, _H, vw, ew);
        compare = Arrays.asList(eAC, eCG, eGI, eIF);
        assertArrayEquals("incorrect: A*", compare.toArray(), path.toArray());
        path = Graphs.<Place, String>
            shortestPath(g, vA, vK, _H, vw, ew);
        compare = Arrays.asList(eAC, eCG, eGK);
        assertArrayEquals("incorrect: A*", compare.toArray(), path.toArray());
    }


    /** Adds an edge between V0 and V1 in graph G designated by String S
     *  with weight determined by the Distancer D. */
    private static Graph<WeightablePlace, WeightedPath>.Edge
    addDistancedPath(Graph<WeightablePlace, WeightedPath> g,
                Distancer<Place> d,
                Graph<WeightablePlace, WeightedPath>.Vertex v0,
                Graph<WeightablePlace, WeightedPath>.Vertex v1,
                String s) {
        return g.add(v0, v1, new WeightedPath(s,
                    d.dist(v0.getLabel(), v1.getLabel())));
    }

    /** Adds an edge between V0 and V1 in graph G designated by String S
     *  with weight determined by the Distancer D. Adds the edges weight to
     *  _edgeWeights. */
    private Graph<Place, String>.Edge
    addDistancedPathNotWeightable(Graph<Place, String> g,
                Distancer<Place> d,
                Graph<Place, String>.Vertex v0,
                Graph<Place, String>.Vertex v1,
                String s) {
        Double distance = d.dist(v0.getLabel(), v1.getLabel());
        Graph<Place, String>.Edge e =
            g.add(v0, v1, s);
        _edgeWeights.put(e.getLabel(), distance);
        return e;
    }

    /** Gets weights for Edges from _edgeWeights. */
    private class MapEdgeWeighting implements Weighting<String> {
        @Override
        public double weight(String s) {
            return _edgeWeights.get(s);
        }
    }

    /** Gets and sets weights for Vertices from/in _vertWeights. */
    private class MapVertexWeighter implements Weighter<Place> {
        @Override
        public void setWeight(Place s, double v) {
            _vertWeights.put(s, v);
        }

        @Override
        public double weight(Place s) {
            return _vertWeights.get(s);
        }
    }

    /** Initializes weight maps for unweightable traversals. */
    private void initializeWeightMaps() {
        _edgeWeights = new HashMap<String, Double>();
        _vertWeights = new HashMap<Place, Double>();
    }

    /** Class that represents a place designated by a String with
     *  lat/lng coordinates. */
    private static class Place implements Comparable<Place> {

        /** Constructs a Place with x-position X and y-position Y
         *  designated by String S. */
        Place(String s, int x, int y) {
            _string = s;
            _y = y;
            _x = x;
        }

        /** Returns the lat/lng of this WeightablePlace. */
        public double[] getCoords() {
            return new double[]{ _x, _y };
        }

        @Override
        public int compareTo(Place wc) {
            return _string.compareTo(wc.toString());
        }

        @Override
        public String toString() {
            return _string;
        }

        /** String of this WeightablePlace. */
        private final String _string;
        /** X-Position of this WeightablePlace. */
        private final int _x;
        /** Y-Position of this WeightablePlace. */
        private final int _y;
    }

    /** Class that represents a Weightable place designated by a String with
     *  lat/lng coordinates. */
    private static class WeightablePlace extends Place implements Weightable {

        /** Constructs a WeightablePlace with x-position X and y-position Y
         *  designated by String S. */
        WeightablePlace(String s, int x, int y) {
            super(s, x, y);
            _weight = 0;
        }

        @Override
        public void setWeight(double w) {
            _weight = w;
        }

        @Override
        public double weight() {
            return _weight;
        }

        /** Weight of this WeightablePlace. */
        private double _weight;
    }

    /** Class that represents a Weighted path designated by a String. */
    private static class WeightedPath
            implements Weighted, Comparable<WeightedPath> {

        /** Constructs a WeightedPlace with weight W designated by
         *  Character C. */
        WeightedPath(String s, double w) {
            _string = s;
            _weight = w;
        }

        @Override
        public double weight() {
            return _weight;
        }

        @Override
        public int compareTo(WeightedPath wp) {
            return _string.compareTo(wp.toString());
        }

        @Override
        public String toString() {
            return _string;
        }

        /** String of this WeightablePlace. */
        private final String _string;
        /** Weight of this WeightablePlace. */
        private final double _weight;
    }

    private final Distancer<Place> _H =
        new Distancer<Place>() {
            @Override
            public double dist(Place p0, Place p1) {
                double[] p0Coords = p0.getCoords();
                double[] p1Coords = p1.getCoords();
                return Math.hypot(p1Coords[0] - p0Coords[0],
                        p1Coords[1] - p0Coords[1]);
            }
        };

    private Map<String, Double> _edgeWeights;
    private Map<Place, Double> _vertWeights;

}
