package trip;

import java.util.Map;

import java.util.LinkedList;
import java.util.List;

import java.util.Scanner;

import java.util.NoSuchElementException;

import graph.Graph;
import graph.Graphs;
import graph.Distancer;

public class TripFinder {

    TripFinder(Graph<Location, Road> mapGraph,
            Map<String, Graph<Location, Road>.Vertex> vmap) {
        _mapGraph = mapGraph;
        _vmap = vmap;
    }

    public List<Trip> findTrips() {
        _scn = new Scanner(System.in).useDelimiter(",\\s+");
        List<Trip> result = new LinkedList<Trip>();
        String start, end;
        start = end = null;
        try {
            start = _scn.next();
            end = _scn.next();
        } catch (NoSuchElementException e) {
            throw new TripException("no trips to be found");
        }

        result.add(findTrip(start, end));
        while (_scn.hasNext()) {
            start = end;
            end = _scn.next();
            result.add(findTrip(start, end));
        }

        return result;
    }

    private Trip findTrip(String start, String end) {
        List<Graph<Location, Road>.Edge> elist =
            Graphs.shortestPath(_mapGraph, _vmap.get(start), 
                    _vmap.get(end), new LocationDistancer());
        if (elist == null) {
            String error = String.format("impossible to travel from %s to %s.",
                    start, end);
            throw new TripException(error);
        }

        Trip result = new Trip(end);
        for (Graph<Location, Road>.Edge e : elist) {
            Road r = e.getLabel();
            result.addRoad(r, getDirection(r));
        }

        return result;
    }

    private char getDirection(Road road) {
        return road.direction().charAt(1);
    }

    private static class LocationDistancer implements Distancer<Location> {
        @Override
        public double dist(Location l0, Location l1) {
            return Math.hypot(l1.xPos() - l0.xPos(),
                    l1.yPos() - l0.yPos());
        }
    }

    /** Graph representing the map. */
    private final Graph<Location, Road> _mapGraph;
    /** Maps the name of a Location to its corresponding vertex in 
     *  _mapGraph. */
    private final Map<String, Graph<Location, Road>.Vertex> _vmap;
    /** Scanner for input from System.in. */
    private Scanner _scn;

}
