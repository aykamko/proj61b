package graph;

import org.junit.Test;
import ucb.junit.textui;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/* You MAY add public @Test methods to this class.  You may also add
 * additional public classes containing "Testing" in their name. These
 * may not be part of your graph package per se (that is, it must be
 * possible to remove them and still have your package work). */

/** Unit tests for the graph package.
 *  @author
 */
public class Testing {

    /** Run all JUnit tests in the graph package. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(graph.Testing.class));
    }

    // Add tests.  Here's a sample.

    @Test
    public void emptyGraph() {
        DirectedGraph g = new DirectedGraph();
        assertEquals("Initial graph has vertices", 0, g.vertexSize());
        assertEquals("Initial graph has edges", 0, g.edgeSize());
    }

    @Test
    public void addVertices() {
        UndirectedGraph<Integer, Integer> g =
            new UndirectedGraph<Integer, Integer>();
        g.add(5);
        assertEquals("Graph did not add vertex", 1, g.vertexSize());
        assertEquals("Graph has edges", 0, g.edgeSize());
        
        List<Graph.Vertex> vertices = new ArrayList<Graph.Vertex>();
        for (Graph.Vertex v : g.vertices()) {
            vertices.add(v);
        }

        assertEquals("Iterator over vertices is wrong", 1, vertices.size());
        assertEquals("Graph did not add proper vertex",
                vertices.get(0), g.new Vertex(5));
    }

}
