package trip;

/* You MAY add public @Test methods to this class.  You may also add
 * additional public classes containing "Testing" in their name. These
 * may not be part of your trip package per se (that is, it must be
 * possible to remove them and still have your package work). */

import org.junit.Test;
import ucb.junit.textui;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.LinkedList;

import static trip.Direction.*;

import graph.Graph;
import graph.DirectedGraph;

/** Unit tests for the trip package.
 *  @author Aleks Kamko
 */
public class Testing {

    /** Run all JUnit tests in the graph package. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(trip.Testing.class));
    }

    @Test
    public void testBuildMapGraph() {
        List<Road> roads = new LinkedList<Road>();
        List<Location> locations = new LinkedList<Location>();
        DirectedGraph<Location, Road> g =
            new DirectedGraph<Location, Road>();
        Map<String, Graph<Location, Road>.Vertex> vmap =
            new HashMap<String, Graph<Location, Road>.Vertex>();

        Road r1 = new Road("Berkeley", "I-80", 50.0, SOUTH, "Clovis");
        roads.add(r1);
        Road r2 = new Road("Berkeley", "I-5", 5.0, WEST, "San_Francisco");
        roads.add(r2);
        Road r3 = new Road("Clovis", "CA99", 52.0, NORTH, "San_Francisco");
        roads.add(r3);

        Location l1 = new Location("Berkeley", 0.0, 0.0);
        locations.add(l1);
        Location l2 = new Location("Clovis", 40.0, -100.0);
        locations.add(l2);
        Location l3 = new Location("San_Francisco", -5.0, -5.0);
        locations.add(l3);

        MapGraphBuilder.buildMapGraph(roads, locations, g, vmap);

        Graph<Location, Road>.Vertex berkeley, clovis, sf;
        berkeley = vmap.get("Berkeley");
        clovis = vmap.get("Clovis");
        sf = vmap.get("San_Francisco");

        assertTrue("incorrect: MapGraphBuilder.buildMapGraph()",
                g.contains(berkeley, clovis));
        assertTrue("incorrect: MapGraphBuilder.buildMapGraph()",
                g.contains(clovis, sf));
        assertTrue("incorrect: MapGraphBuilder.buildMapGraph()",
                g.contains(berkeley, sf));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testTripCollapse() {
        Trip trip = new Trip("A", "B");

        Road r1 = new Road("C", "Road", 10.0, NORTH, "D");
        trip.addRoad(r1);
        Road r2 = new Road("D", "Road", 20.0, NORTH, "F");
        trip.addRoad(r2);

        LinkedList<Road> path =
            (LinkedList<Road>) accessPrivateField(trip, "_path");

        assertEquals("incorrect: trip.addRoad()",
                1, path.size());
        Road collapsed = path.get(0);
        assertEquals("incorrect: trip.addRoad()",
                new Double(30.0), new Double(collapsed.length()));
        assertEquals("incorrect: trip.addRoad()",
                NORTH, collapsed.direction());
        assertEquals("incorrect: trip.addRoad()",
                "Road", collapsed.name());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFindTrip1() {
        List<Road> roads = new LinkedList<Road>();
        List<Location> locations = new LinkedList<Location>();
        DirectedGraph<Location, Road> g =
            new DirectedGraph<Location, Road>();
        Map<String, Graph<Location, Road>.Vertex> vmap =
            new HashMap<String, Graph<Location, Road>.Vertex>();

        Road r1 = new Road("A", "Road", 10.0, NORTH, "B");
        roads.add(r1);
        Road r2 = new Road("B", "Road", 20.0, NORTH, "C");
        roads.add(r2);

        Location l1 = new Location("A", 0.0, 0.0);
        locations.add(l1);
        Location l2 = new Location("B", 0.0, 10.0);
        locations.add(l2);
        Location l3 = new Location("C", 0.0, 20.0);
        locations.add(l3);

        MapGraphBuilder.buildMapGraph(roads, locations, g, vmap);
        TripFinder tf = new TripFinder(g, vmap);

        Trip trip =
            (Trip) callPrivateMethod(tf, "findTrip",
                    new Class<?>[]{ String.class,
                                    String.class },
                                    "A", "C");
        LinkedList<Road> path =
            (LinkedList<Road>) accessPrivateField(trip, "_path");
        assertEquals("incorrect: trip.addRoad()",
                1, path.size());
        Road route = path.get(0);
        assertEquals("incorrect: trip.addRoad()",
                new Double(30.0), new Double(route.length()));
        assertEquals("incorrect: trip.addRoad()",
                NORTH, route.direction());
        assertEquals("incorrect: trip.addRoad()",
                "Road", route.name());
    }

    @Test (expected = TripException.class)
    public void testFindTrip2() {
        List<Road> roads = new LinkedList<Road>();
        List<Location> locations = new LinkedList<Location>();
        DirectedGraph<Location, Road> g =
            new DirectedGraph<Location, Road>();
        Map<String, Graph<Location, Road>.Vertex> vmap =
            new HashMap<String, Graph<Location, Road>.Vertex>();

        Road r1 = new Road("A", "Road", 10.0, NORTH, "B");
        roads.add(r1);

        Location l1 = new Location("A", 0.0, 0.0);
        locations.add(l1);
        Location l2 = new Location("B", 0.0, 10.0);
        locations.add(l2);
        Location l3 = new Location("C", 0.0, 20.0);
        locations.add(l3);

        MapGraphBuilder.buildMapGraph(roads, locations, g, vmap);
        TripFinder tf = new TripFinder(g, vmap);

        Trip trip =
            (Trip) callPrivateMethod(tf, "findTrip",
                    new Class<?>[]{ String.class,
                                    String.class },
                                    "A", "C");
    }

    /** Returns a castable Object that wraps the private field NAME of the
     *  given object OBJ. Throws a TripException if OBJ throws an error on
     *  access. */
    private Object accessPrivateField(Object obj, String name) {
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new TripException();
        }
    }

    /** Returns the castable Object returned by invoking the private method
     *  NAME with parameters PARAMS of object OBJ with arguments ARGS. Throws
     *  a TripException if OBJ throws an error on access. */
    private Object callPrivateMethod(Object obj, String name,
            Class<?>[] params, Object... args) {
        try {
            Method method = obj.getClass().getDeclaredMethod(name, params);
            method.setAccessible(true);
            return method.invoke(obj, (Object[]) args);
        } catch (NoSuchMethodException | InvocationTargetException
            | IllegalAccessException e) {
            throw new TripException();
        }
    }

}
