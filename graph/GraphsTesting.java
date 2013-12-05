package graph;

import org.junit.Test;
import ucb.junit.textui;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import java.util.Arrays;

import java.util.Comparator;

/* You MAY add public @Test methods to this class.  You may also add
 * additional public classes containing "Testing" in their name. These
 * may not be part of your graph package per se (that is, it must be
 * possible to remove them and still have your package work). */

/** Unit tests for the Graphs.java (A* Traversal).
 *  @author Aleks Kamko
 */
public class GraphsTesting {

    @Test
    public void shortestPath() {
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
            g.add(new WeightablePlace('A', 3, 6));
        Graph<WeightablePlace, WeightedPath>.Vertex vB =
            g.add(new WeightablePlace('B', 5, 2));
        Graph<WeightablePlace, WeightedPath>.Vertex vC =
            g.add(new WeightablePlace('C', 8, 5));
        Graph<WeightablePlace, WeightedPath>.Vertex vD =
            g.add(new WeightablePlace('D', 9, 2));
        Graph<WeightablePlace, WeightedPath>.Vertex vE =
            g.add(new WeightablePlace('E', 10, 4));
        Graph<WeightablePlace, WeightedPath>.Vertex vF =
            g.add(new WeightablePlace('F', 13, 3));
        Graph<WeightablePlace, WeightedPath>.Vertex vG =
            g.add(new WeightablePlace('G', 10, 7));
        Graph<WeightablePlace, WeightedPath>.Vertex vH =
            g.add(new WeightablePlace('H', 5, 11));
        Graph<WeightablePlace, WeightedPath>.Vertex vI =
            g.add(new WeightablePlace('I', 8, 15));
        Graph<WeightablePlace, WeightedPath>.Vertex vJ =
            g.add(new WeightablePlace('J', 11, 12));
        Graph<WeightablePlace, WeightedPath>.Vertex vK =
            g.add(new WeightablePlace('K', 12, 8));

        Graph<WeightablePlace, WeightedPath>.Edge eA =
            addDistancedPath(g, d, vA, vB, 'A');
        Graph<WeightablePlace, WeightedPath>.Edge eB =
            addDistancedPath(g, d, vA, vC, 'B');
        Graph<WeightablePlace, WeightedPath>.Edge eC =
            addDistancedPath(g, d, vB, vC, 'C');
        Graph<WeightablePlace, WeightedPath>.Edge eD =
            addDistancedPath(g, d, vD, vE, 'D');
        Graph<WeightablePlace, WeightedPath>.Edge eE =
            addDistancedPath(g, d, vC, vG, 'E');
        Graph<WeightablePlace, WeightedPath>.Edge eF =
            addDistancedPath(g, d, vA, vH, 'F');
        Graph<WeightablePlace, WeightedPath>.Edge eG =
            addDistancedPath(g, d, vH, vJ, 'G');
        Graph<WeightablePlace, WeightedPath>.Edge eH =
            addDistancedPath(g, d, vG, vJ, 'H');
        Graph<WeightablePlace, WeightedPath>.Edge eI =
            addDistancedPath(g, d, vI, vJ, 'I');
        Graph<WeightablePlace, WeightedPath>.Edge eJ =
            addDistancedPath(g, d, vI, vG, 'J');
        Graph<WeightablePlace, WeightedPath>.Edge eK =
            addDistancedPath(g, d, vI, vF, 'K');
        Graph<WeightablePlace, WeightedPath>.Edge eL =
            addDistancedPath(g, d, vG, vK, 'L');

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
            Arrays.asList(eF, eG, eI);
        assertArrayEquals("incorrect: A*", compare.toArray(), path.toArray());

        path = Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, vA, vF, d);
        compare = Arrays.asList(eF, eG, eI, eK);
        assertArrayEquals("incorrect: A*", compare.toArray(), path.toArray());

        path = Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, vA, vK, d);
        compare = Arrays.asList(eB, eE, eL);
        assertArrayEquals("incorrect: A*", compare.toArray(), path.toArray());
    }

    /** Adds an edge between V0 and V1 in graph G designated by Character C 
     *  with weight determined by the Distancer D. */
    private static Graph<WeightablePlace, WeightedPath>.Edge
        addDistancedPath(Graph<WeightablePlace, WeightedPath> g, 
                Distancer<WeightablePlace> d,
                Graph<WeightablePlace, WeightedPath>.Vertex v0,
                Graph<WeightablePlace, WeightedPath>.Vertex v1,
                Character c) {
        return g.add(v0, v1, new WeightedPath(c,
                    d.dist(v0.getLabel(), v1.getLabel())));
    }

    /** Class that represents a Weightable place designated by a Character with 
     *  lat/lng coordinates. */
    private static class WeightablePlace
            implements Weightable, Comparable<WeightablePlace> {

        /** Constructs a WeightablePlace with x-position X and y-position Y 
         *  designated by Character C. */
        WeightablePlace(char c, int x, int y) {
            _char = c;
            _weight = 0;
            _y = y;
            _x = x;
        }

        /** Gets the Character of this WeightablePlace. */
        public Character getChar() {
            return _char;
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
            return _char.compareTo(wc.getChar());
        }

        @Override
        public String toString() {
            return String.valueOf(_char);
        }

        /** Character of this WeightablePlace. */
        private final Character _char;
        /** X-Position of this WeightablePlace. */
        private final int _x;
        /** Y-Position of this WeightablePlace. */
        private final int _y;
        /** Weight of this WeightablePlace. */
        private double _weight;
    }

    /** Class that represents a Weighted path designated by a Character. */
    private static class WeightedPath
            implements Weighted, Comparable<WeightedPath> {

        /** Constructs a WeightedPlace with weight W designated by
         *  Character C. */
        WeightedPath(char c, double w) {
            _char = c;
            _weight = w;
        }

        /** Gets the Character of this WeightablePlace. */
        public Character getChar() {
            return _char;
        }

        @Override
        public double weight() {
            return _weight;
        }

        @Override
        public int compareTo(WeightedPath wp) {
            return _char.compareTo(wp.getChar());
        }

        @Override
        public String toString() {
            return String.valueOf(_char);
        }

        /** Character of this WeightablePlace. */
        private final Character _char;
        /** Weight of this WeightablePlace. */
        private final double _weight;
    }

}
