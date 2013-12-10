package trip;

import java.util.Map;

import java.util.LinkedList;
import java.util.List;

import java.util.Scanner;

import java.util.NoSuchElementException;

import graph.Graph;
import graph.Graphs;
import graph.Distancer;

/** TripFinder uses A* to find the shortest trip between every two input
 *  locations.
 *  @author Aleks Kamko
 */
public class TripFinder {

    /** Default constructor for TripFinder. Takes in a graph of the map to
     *  traverse over (MAPGRAPH), and a Map<String, Vertex> VMAP that maps the
     *  name of a Location in MAPGRAPH to its corresponding Vertex in
     *  MAPGRAPH. */
    TripFinder(Graph<Location, Road> mapGraph,
            Map<String, Graph<Location, Road>.Vertex> vmap) {
        _mapGraph = mapGraph;
        _vmap = vmap;
    }

    /** Returns a List of Trips between each two successive inputted
     *  locations. */
    public List<Trip> findTrips() {
        _scn = new Scanner(System.in).useDelimiter(",\\s+");
        List<Trip> result = new LinkedList<Trip>();
        String start, end;
        start = end = null;
        try {
            start = _scn.next().trim();
            end = _scn.next().trim();
        } catch (NoSuchElementException e) {
            throw new TripException("no trips to be found");
        }

        result.add(findTrip(start, end));
        while (_scn.hasNext()) {
            start = end;
            end = _scn.next().trim();
            result.add(findTrip(start, end));
        }

        return result;
    }

    /** Returns the Trip between starting location START and ending location
     *  END. Computed using the A* algorithm. */
    private Trip findTrip(String start, String end) {
        //FIXME: find out why ZERO_DISTANCER works, and replace it
        List<Graph<Location, Road>.Edge> elist =
            Graphs.shortestPath(_mapGraph, _vmap.get(start),
                    _vmap.get(end), Graphs.ZERO_DISTANCER);
        if (elist == null) {
            String error = String.format("impossible to travel from %s to %s.",
                    start, end);
            throw new TripException(error);
        }

        Trip result = new Trip(start, end);
        for (Graph<Location, Road>.Edge e : elist) {
            Road r = e.getLabel();
            result.addRoad(r);
        }

        return result;
    }

    /** Computes the distance between two Locations by taking the hypotenuse
     *  between their two coordinate positions. */
    private static class LocationDistancer implements Distancer<Location> {
        @Override
        public double dist(Location l0, Location l1) {
            return Math.hypot(l1.xPos() - l0.xPos(),
                    l1.yPos() - l0.yPos());
        }
    }

    /** Graph representing the map. */
    private final Graph<Location, Road> _mapGraph;
    /** Maps the name of a Location to its corresponding vertex in _mapGraph. */
    private final Map<String, Graph<Location, Road>.Vertex> _vmap;
    /** Scanner for input from System.in. */
    private Scanner _scn;

}
