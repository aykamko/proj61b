package graph;

import org.junit.Test;
import ucb.junit.textui;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import java.util.Arrays;

import java.util.Comparator;

/** Unit tests for the Graphs.java (A* Traversal).
 *  @author Aleks Kamko
 */
public class GraphsTesting {

    @Test
    public void shortestPathUndirected() {
        UndirectedGraph<WeightablePlace, WeightedPath> g =
            new UndirectedGraph<WeightablePlace, WeightedPath>();

        Distancer<WeightablePlace> d = new Distancer<WeightablePlace>() {
            @Override
            public double dist(WeightablePlace p0, WeightablePlace p1) {
                double[] p0Coords = p0.getCoords();
                double[] p1Coords = p1.getCoords();
                return Math.hypot(p1Coords[0] - p0Coords[0],
                        p1Coords[1] - p0Coords[1]);
            }
        };

        Graph<WeightablePlace, WeightedPath>.Vertex vA =
            g.add(new WeightablePlace("A", 3, 6));
        Graph<WeightablePlace, WeightedPath>.Vertex vB =
            g.add(new WeightablePlace("B", 5, 2));
        Graph<WeightablePlace, WeightedPath>.Vertex vC =
            g.add(new WeightablePlace("C", 8, 5));
        Graph<WeightablePlace, WeightedPath>.Vertex vD =
            g.add(new WeightablePlace("D", 9, 2));
        Graph<WeightablePlace, WeightedPath>.Vertex vE =
            g.add(new WeightablePlace("E", 10, 4));
        Graph<WeightablePlace, WeightedPath>.Vertex vF =
            g.add(new WeightablePlace("F", 13, 3));
        Graph<WeightablePlace, WeightedPath>.Vertex vG =
            g.add(new WeightablePlace("G", 10, 7));
        Graph<WeightablePlace, WeightedPath>.Vertex vH =
            g.add(new WeightablePlace("H", 5, 11));
        Graph<WeightablePlace, WeightedPath>.Vertex vI =
            g.add(new WeightablePlace("I", 8, 15));
        Graph<WeightablePlace, WeightedPath>.Vertex vJ =
            g.add(new WeightablePlace("J", 11, 12));
        Graph<WeightablePlace, WeightedPath>.Vertex vK =
            g.add(new WeightablePlace("K", 12, 8));

        Graph<WeightablePlace, WeightedPath>.Edge eAB =
            addDistancedPath(g, d, vA, vB, "AB");
        Graph<WeightablePlace, WeightedPath>.Edge eAC =
            addDistancedPath(g, d, vA, vC, "AC");
        Graph<WeightablePlace, WeightedPath>.Edge eBC =
            addDistancedPath(g, d, vB, vC, "BC");
        Graph<WeightablePlace, WeightedPath>.Edge eDE =
            addDistancedPath(g, d, vD, vE, "DE");
        Graph<WeightablePlace, WeightedPath>.Edge eCG =
            addDistancedPath(g, d, vC, vG, "CG");
        Graph<WeightablePlace, WeightedPath>.Edge eAH =
            addDistancedPath(g, d, vA, vH, "AH");
        Graph<WeightablePlace, WeightedPath>.Edge eHJ =
            addDistancedPath(g, d, vH, vJ, "HJ");
        Graph<WeightablePlace, WeightedPath>.Edge eGJ =
            addDistancedPath(g, d, vG, vJ, "GJ");
        Graph<WeightablePlace, WeightedPath>.Edge eIJ =
            addDistancedPath(g, d, vI, vJ, "IJ");
        Graph<WeightablePlace, WeightedPath>.Edge eIG =
            addDistancedPath(g, d, vI, vG, "IG");
        Graph<WeightablePlace, WeightedPath>.Edge eIF =
            addDistancedPath(g, d, vI, vF, "IF");
        Graph<WeightablePlace, WeightedPath>.Edge eGK =
            addDistancedPath(g, d, vG, vK, "GK");

        List<Graph<WeightablePlace, WeightedPath>.Edge> path =
            Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, vA, vE, d);
        assertTrue("incorrect: A*", path == null);

        path = Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, vA, vD, d);
        assertTrue("incorrect: A*", path == null);

        path = Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, vA, vI, d);
        List<Graph<WeightablePlace, WeightedPath>.Edge> compare =
            Arrays.asList(eAH, eHJ, eIJ);
        assertArrayEquals("incorrect: A*", compare.toArray(), path.toArray());

        path = Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, vA, vF, d);
        compare = Arrays.asList(eAH, eHJ, eIJ, eIF);
        assertArrayEquals("incorrect: A*", compare.toArray(), path.toArray());

        path = Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, vA, vK, d);
        compare = Arrays.asList(eAC, eCG, eGK);
        assertArrayEquals("incorrect: A*", compare.toArray(), path.toArray());
    }

    @Test
    public void shortestPathDirected() {
        DirectedGraph<WeightablePlace, WeightedPath> g =
            new DirectedGraph<WeightablePlace, WeightedPath>();

        Distancer<WeightablePlace> d = new Distancer<WeightablePlace>() {
            @Override
            public double dist(WeightablePlace p0, WeightablePlace p1) {
                double[] p0Coords = p0.getCoords();
                double[] p1Coords = p1.getCoords();
                return Math.hypot(p1Coords[0] - p0Coords[0],
                        p1Coords[1] - p0Coords[1]);
            }
        };

        Graph<WeightablePlace, WeightedPath>.Vertex vA =
            g.add(new WeightablePlace("A", 3, 6));
        Graph<WeightablePlace, WeightedPath>.Vertex vB =
            g.add(new WeightablePlace("B", 5, 2));
        Graph<WeightablePlace, WeightedPath>.Vertex vC =
            g.add(new WeightablePlace("C", 8, 5));
        Graph<WeightablePlace, WeightedPath>.Vertex vD =
            g.add(new WeightablePlace("D", 9, 2));
        Graph<WeightablePlace, WeightedPath>.Vertex vE =
            g.add(new WeightablePlace("E", 10, 4));
        Graph<WeightablePlace, WeightedPath>.Vertex vF =
            g.add(new WeightablePlace("F", 13, 3));
        Graph<WeightablePlace, WeightedPath>.Vertex vG =
            g.add(new WeightablePlace("G", 10, 7));
        Graph<WeightablePlace, WeightedPath>.Vertex vH =
            g.add(new WeightablePlace("H", 5, 11));
        Graph<WeightablePlace, WeightedPath>.Vertex vI =
            g.add(new WeightablePlace("I", 8, 15));
        Graph<WeightablePlace, WeightedPath>.Vertex vJ =
            g.add(new WeightablePlace("J", 11, 12));
        Graph<WeightablePlace, WeightedPath>.Vertex vK =
            g.add(new WeightablePlace("K", 12, 8));

        Graph<WeightablePlace, WeightedPath>.Edge eAB =
            addDistancedPath(g, d, vA, vB, "AB");
        Graph<WeightablePlace, WeightedPath>.Edge eAC =
            addDistancedPath(g, d, vA, vC, "AC");
        Graph<WeightablePlace, WeightedPath>.Edge eCB =
            addDistancedPath(g, d, vC, vB, "CB");
        Graph<WeightablePlace, WeightedPath>.Edge eDE =
            addDistancedPath(g, d, vD, vE, "DE");
        Graph<WeightablePlace, WeightedPath>.Edge eCG =
            addDistancedPath(g, d, vC, vG, "CG");
        Graph<WeightablePlace, WeightedPath>.Edge eHA =
            addDistancedPath(g, d, vH, vA, "HA");
        Graph<WeightablePlace, WeightedPath>.Edge eJH =
            addDistancedPath(g, d, vJ, vH, "JH");
        Graph<WeightablePlace, WeightedPath>.Edge eGJ =
            addDistancedPath(g, d, vG, vJ, "GJ");
        Graph<WeightablePlace, WeightedPath>.Edge eIJ =
            addDistancedPath(g, d, vI, vJ, "IJ");
        Graph<WeightablePlace, WeightedPath>.Edge eGI =
            addDistancedPath(g, d, vG, vI, "GI");
        Graph<WeightablePlace, WeightedPath>.Edge eIF =
            addDistancedPath(g, d, vI, vF, "IF");
        Graph<WeightablePlace, WeightedPath>.Edge eGK =
            addDistancedPath(g, d, vG, vK, "GK");

        List<Graph<WeightablePlace, WeightedPath>.Edge> path =
            Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, vA, vE, d);
        assertTrue("incorrect: A*", path == null);

        path = Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, vA, vD, d);
        assertTrue("incorrect: A*", path == null);

        path = Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, vK, vG, d);
        assertTrue("incorrect: A*", path == null);

        path = Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, vB, vA, d);
        assertTrue("incorrect: A*", path == null);

        path = Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, vA, vI, d);
        List<Graph<WeightablePlace, WeightedPath>.Edge> compare =
            Arrays.asList(eAC, eCG, eGI);
        assertArrayEquals("incorrect: A*", compare.toArray(), path.toArray());

        path = Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, vA, vF, d);
        compare = Arrays.asList(eAC, eCG, eGI, eIF);
        assertArrayEquals("incorrect: A*", compare.toArray(), path.toArray());

        path = Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, vA, vK, d);
        compare = Arrays.asList(eAC, eCG, eGK);
        assertArrayEquals("incorrect: A*", compare.toArray(), path.toArray());
    }

    /** Adds an edge between V0 and V1 in graph G designated by String S 
     *  with weight determined by the Distancer D. */
    private static Graph<WeightablePlace, WeightedPath>.Edge
        addDistancedPath(Graph<WeightablePlace, WeightedPath> g, 
                Distancer<WeightablePlace> d,
                Graph<WeightablePlace, WeightedPath>.Vertex v0,
                Graph<WeightablePlace, WeightedPath>.Vertex v1,
                String s) {
        return g.add(v0, v1, new WeightedPath(s,
                    d.dist(v0.getLabel(), v1.getLabel())));
    }

    /** Class that represents a Weightable place designated by a String with 
     *  lat/lng coordinates. */
    private static class WeightablePlace
            implements Weightable, Comparable<WeightablePlace> {

        /** Constructs a WeightablePlace with x-position X and y-position Y 
         *  designated by String S. */
        WeightablePlace(String s, int x, int y) {
            _string = s;
            _weight = 0;
            _y = y;
            _x = x;
        }

        /** Returns the lat/lng of this WeightablePlace. */
        public double[] getCoords() {
            return new double[]{ _x, _y };
        }

        @Override
        public void setWeight(double w) {
            _weight = w;
        }

        @Override
        public double weight() {
            return _weight;
        }

        @Override
        public int compareTo(WeightablePlace wc) {
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

}
