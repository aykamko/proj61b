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

/** Unit tests for the graph package.
 *  @author Aleks Kamko
 */
public class Testing {

    /** Run all JUnit tests in the graph package. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(graph.Testing.class,
                                      graph.TraversalTesting.class,
                                      graph.GraphsTesting.class));
    }

    @Test
    public void emptyGraph() {
        DirectedGraph g = new DirectedGraph();
        assertEquals("Initial graph has vertices", 0, g.vertexSize());
        assertEquals("Initial graph has edges", 0, g.edgeSize());
    }

    @Test
    public void addVertices1() {
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
                vertices.get(0).getLabel(), 5);
    }

    @Test
    public void addVertices2() {
        UndirectedGraph<Integer, Integer> g =
            new UndirectedGraph<Integer, Integer>();
        Integer[] labels = new Integer[]{ 0, 0, 1, 2, 3, 3, 3, 4, 6, 7 };
        for (Integer l : labels) {
            g.add(l);
        }
        assertEquals("Graph did not add vertices", 10, g.vertexSize());
        assertEquals("Graph has edges", 0, g.edgeSize());

        List<Graph.Vertex> vertices = new ArrayList<Graph.Vertex>();
        for (Graph.Vertex v : g.vertices()) {
            vertices.add(v);
        }

        assertEquals("Iterator over vertices is wrong", 10, vertices.size());
        for (int i = 0; i < vertices.size(); i += 1) {
            Graph.Vertex v = vertices.get(i);
            assertEquals("Graph did not add proper vertex",
                    v.getLabel(), labels[i]);
        }
    }

    @Test
    public void addEdgesUndirected() {
        UndirectedGraph<Integer, Integer> g =
            new UndirectedGraph<Integer, Integer>();
        Integer[] vLabels = new Integer[]{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        List<Graph<Integer, Integer>.Vertex> vlist =
            new ArrayList<Graph<Integer, Integer>.Vertex>();
        for (int i = 0; i < vLabels.length; i += 1) {
            vlist.add(g.add(vLabels[i]));
        }

        Integer[] eLabels = new Integer[]{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        for (int i = 0; i < eLabels.length; i += 1) {
            g.add(vlist.get(i), vlist.get((i + 1) % 10), eLabels[i]);
        }

        assertEquals("Graph did not add vertices", 10, g.vertexSize());
        assertEquals("Graph did not add edges", 10, g.edgeSize());

        Graph<Integer, Integer>.Vertex v, u;
        Integer label;
        for (int i = 0; i < eLabels.length; i += 1) {
            v = vlist.get(i);
            u = vlist.get((i + 1) % 10);
            label = eLabels[i];
            assertEquals("Vertex did not add edge", g.degree(v), 2);
            assertEquals("Vertex did not add edge", g.degree(u), 2);
            assertTrue("Graph did not add an edge", g.contains(v, u));
            assertTrue("Graph did not add an edge", g.contains(v, u, label));
            assertTrue("Graph did not add an edge", g.contains(u, v));
            assertTrue("Graph did not add an edge", g.contains(u, v, label));
        }
    }

    @Test
    public void addEdgesDirected() {
        DirectedGraph<Integer, Integer> g =
            new DirectedGraph<Integer, Integer>();
        Integer[] vLabels = new Integer[]{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        List<Graph<Integer, Integer>.Vertex> vlist =
            new ArrayList<Graph<Integer, Integer>.Vertex>();
        for (int i = 0; i < vLabels.length; i += 1) {
            vlist.add(g.add(vLabels[i]));
        }

        Integer[] eLabels = new Integer[]{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        for (int i = 0; i < eLabels.length; i += 1) {
            g.add(vlist.get(i), vlist.get((i + 1) % 10), eLabels[i]);
        }

        assertEquals("Graph did not add vertices", 10, g.vertexSize());
        assertEquals("Graph did not add edges", 10, g.edgeSize());

        Graph<Integer, Integer>.Vertex v, u;
        Integer label;
        for (int i = 0; i < eLabels.length; i += 1) {
            v = vlist.get(i);
            u = vlist.get((i + 1) % 10);
            label = eLabels[i];
            assertEquals("Vertex did not add edge", g.outDegree(v), 1);
            assertEquals("Vertex did not add edge", g.inDegree(v), 1);
            assertEquals("Vertex did not add edge", g.outDegree(u), 1);
            assertEquals("Vertex did not add edge", g.inDegree(u), 1);
            assertTrue("Graph did not add an edge", g.contains(v, u));
            assertTrue("Graph did not add an edge", g.contains(v, u, label));
            assertFalse("Graph should not add undirected edge",
                    g.contains(u, v));
            assertFalse("Graph should not add undirected edge",
                    g.contains(u, v, label));
        }
    }

    @Test
    public void orderEdges() {
        UndirectedGraph<Integer, Integer> g =
            new UndirectedGraph<Integer, Integer>();
        Integer[] vLabels = new Integer[]{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        List<Graph<Integer, Integer>.Vertex> vlist =
            new ArrayList<Graph<Integer, Integer>.Vertex>();
        for (int i = 0; i < vLabels.length; i += 1) {
            vlist.add(g.add(vLabels[i]));
        }

        Integer[] eLabels = new Integer[]{ 2, 5, 4, 7, 9, 3, 6, 2, 5, 2 };
        for (int i = 0; i < eLabels.length; i += 1) {
            g.add(vlist.get(i), vlist.get((i + 1) % 10), eLabels[i]);
        }

        assertEquals("Graph did not add vertices", 10, g.vertexSize());
        assertEquals("Graph did not add edges", 10, g.edgeSize());

        Comparator<Integer> integerOrder = Graph.<Integer>naturalOrder();
        g.orderEdges(integerOrder);
        Arrays.sort(eLabels, integerOrder);
        int i = 0;
        for (Graph<Integer, Integer>.Edge e : g.edges()) {
            assertEquals("Edge not order correctly.",
                    e.getLabel(), eLabels[i]);
            i += 1;
        }
    }

    @Test
    public void removeEdge() {
        UndirectedGraph<Integer, Integer> g =
            new UndirectedGraph<Integer, Integer>();
        Integer[] vLabels = new Integer[]{ 0, 1, 2 };
        List<Graph<Integer, Integer>.Vertex> vlist =
            new ArrayList<Graph<Integer, Integer>.Vertex>();
        for (int i = 0; i < vLabels.length; i += 1) {
            vlist.add(g.add(vLabels[i]));
        }

        Integer[] eLabels = new Integer[]{ 1, 2, 2 };
        Graph<Integer, Integer>.Edge e0 =
            g.add(vlist.get(0), vlist.get(1), eLabels[0]);
        Graph<Integer, Integer>.Edge e1 =
            g.add(vlist.get(0), vlist.get(1), eLabels[1]);
        Graph<Integer, Integer>.Edge e2 =
            g.add(vlist.get(2), vlist.get(1), eLabels[2]);

        List<Graph<Integer, Integer>.Edge> elist =
            constructEdgeList(e0, e1, e2);

        allEdgesExistUndirected(g, elist);

        g.remove(e1);
        elist.remove(e1);
        allEdgesExistUndirected(g, elist);

        g.remove(e2);
        elist.remove(e2);
        allEdgesExistUndirected(g, elist);

        g.remove(e0);
        elist.remove(e0);
        allEdgesExistUndirected(g, elist);
    }

    @Test
    public void removeVertex() {
        DirectedGraph<Integer, Integer> g =
            new DirectedGraph<Integer, Integer>();
        Integer[] vLabels = new Integer[]{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        List<Graph<Integer, Integer>.Vertex> vlist =
            new ArrayList<Graph<Integer, Integer>.Vertex>();
        for (int i = 0; i < vLabels.length; i += 1) {
            vlist.add(g.add(vLabels[i]));
        }

        Integer[] eLabels = new Integer[]{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        List<Graph<Integer, Integer>.Edge> elist =
            new ArrayList<Graph<Integer, Integer>.Edge>();
        for (int i = 0; i < eLabels.length; i += 1) {
            elist.add(g.add(vlist.get(0), vlist.get(i), eLabels[i]));
        }

        for (Graph<Integer, Integer>.Vertex v : vlist) {
            assertEquals("Graph did not add edges.", 1, g.inDegree(v));
        }

        assertEquals("Graph did not add edges.", 10, g.outDegree(vlist.get(0)));

        allEdgesExistDirected(g, elist);
        g.remove(vlist.get(0));
        vlist.remove(0);

        assertEquals("Graph did not remove vertex", 9, g.vertexSize());
        assertEquals("Graph did not remove edges", 0, g.edgeSize());
    }

    @SafeVarargs
    private static <VLabel, ELabel>
    List<Graph<VLabel, ELabel>.Edge> constructEdgeList(
                Graph<VLabel, ELabel>.Edge... edges) {

        List<Graph<VLabel, ELabel>.Edge> result =
            new ArrayList<Graph<VLabel, ELabel>.Edge>();
        for (Graph<VLabel, ELabel>.Edge e : edges) {
            result.add(e);
        }
        return result;
    }

    private static <VLabel, ELabel> void edgeExistsDirected(
            Graph<VLabel, ELabel> g, Graph<VLabel, ELabel>.Edge e) {
        Graph<VLabel, ELabel>.Vertex v, u;
        ELabel label = e.getLabel();
        v = e.getV0();
        u = e.getV1();
        assertTrue("Graph did not add an edge", g.contains(v, u));
        assertTrue("Graph did not add an edge", g.contains(v, u, label));
        if (u != v) {
            assertFalse("Graph should not add undirected edge",
                    g.contains(u, v));
            assertFalse("Graph should not add undirected edge",
                    g.contains(u, v, label));
        }
    }

    private static <VLabel, ELabel> void edgeExistsUndirected(
            Graph<VLabel, ELabel> g, Graph<VLabel, ELabel>.Edge e) {
        Graph<VLabel, ELabel>.Vertex v, u;
        ELabel label = e.getLabel();
        v = e.getV0();
        u = e.getV1();
        assertTrue("Graph did not add an edge", g.contains(v, u));
        assertTrue("Graph did not add an ed5e", g.contains(v, u, label));
        if (u != v) {
            assertTrue("Graph did not add an edge", g.contains(u, v));
            assertTrue("Graph did not add an edge", g.contains(u, v, label));
        }
    }

    private static <VLabel, ELabel> void allEdgesExistDirected(
            Graph<VLabel, ELabel> g, List<Graph<VLabel, ELabel>.Edge> elist) {
        for (Graph<VLabel, ELabel>.Edge e : elist) {
            edgeExistsDirected(g, e);
        }
    }

    private static <VLabel, ELabel> void allEdgesExistUndirected(
            Graph<VLabel, ELabel> g, List<Graph<VLabel, ELabel>.Edge> elist) {
        for (Graph<VLabel, ELabel>.Edge e : elist) {
            edgeExistsUndirected(g, e);
        }
    }

}
