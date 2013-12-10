package trip;

import java.util.List;

import java.util.Map;

import graph.Graph;
import graph.DirectedGraph;

/** Class that builds a graph out of a list of roads and a list of locations.
 *  @author Aleks Kamko
 */
public class MapGraphBuilder {

    /** Puts ROADS and LOCATIONS into the DirectedGraph G, and fills VMAP
     *  with a mapping from the name of each location to the vertex
     *  representing it in G. */
    public static void buildMapGraph(List<Road> roads, List<Location> locations,
            DirectedGraph<Location, Road> g, 
            Map<String, Graph<Location, Road>.Vertex> vmap) {

        for (Location loc : locations) {
            vmap.put(loc.place(), g.add(loc));
        }

        String start, end;
        Graph<Location, Road>.Vertex v, u;
        for (Road r : roads) {
            start = r.start();
            end = r.end();
            v = vmap.get(start);
            u = vmap.get(end);
            if (v == null || u == null) {
                throw new TripException("location for road does not exist");
            }

            g.add(v, u, r);
        }
    }

}
