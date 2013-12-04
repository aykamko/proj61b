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

        List<Graph<WeightablePlace, WeightedPath>.Edge> path =
            Graphs.<WeightablePlace, WeightedPath>
            shortestPath(g, null, null, d);
    }

    /** Class that represents a Weightable place designated by a Character with 
     *  lat/lng coordinates. */
    private static class WeightablePlace
            implements Weightable, Comparable<WeightablePlace> {

        /** Constructs a WeightablePlace with weight W, x-position X, and
         *  y-position Y designated by Character C. */
        WeightablePlace(char c, double x, double y, double w) {
            _char = c;
            _weight = w;
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

        /** Character of this WeightablePlace. */
        private final Character _char;
        /** X-Position of this WeightablePlace. */
        private final double _x;
        /** Y-Position of this WeightablePlace. */
        private final double _y;
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

        /** Returns the lat/lng of this WeightablePlace. */
        public double[] getCoords() {
            return new double[]{ _lat, _lng };
        }

        @Override
        public double weight() {
            return _weight;
        }

        @Override
        public int compareTo(WeightedPath wp) {
            return _char.compareTo(wp.getChar());
        }

        /** Character of this WeightablePlace. */
        private Character _char;
        /** Latitude of this WeightablePlace. */
        private double _lat;
        /** Longitude of this WeightablePlace. */
        private double _lng;
        /** Weight of this WeightablePlace. */
        private double _weight;
    }

}
