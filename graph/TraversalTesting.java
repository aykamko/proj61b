package graph;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import java.util.Arrays;

/** Unit tests for the Traversal.java
 *  @author Aleks Kamko
 */
public class TraversalTesting {

    @Test
    public void depthFirstDirected1() {
        DirectedGraph<Character, String> g =
            new DirectedGraph<Character, String>();

        LinkedList<String> visitList = new LinkedList<String>();
        Traversal<Character, String> markTraversal =
            new MarkTraversal(visitList);

        Graph<Character, String>.Vertex vA = g.add('A');
        Graph<Character, String>.Vertex vB = g.add('B');
        Graph<Character, String>.Vertex vC = g.add('C');
        Graph<Character, String>.Vertex vD = g.add('D');
        Graph<Character, String>.Vertex vE = g.add('E');
        Graph<Character, String>.Vertex vF = g.add('F');

        addEdge(g, vA, vB, "AB");
        addEdge(g, vA, vF, "AF");
        addEdge(g, vB, vC, "BC");
        addEdge(g, vC, vD, "CD");
        addEdge(g, vC, vE, "CE");
        addEdge(g, vD, vB, "DB");
        addEdge(g, vD, vE, "DE");
        addEdge(g, vE, vA, "EA");
        addEdge(g, vF, vE, "FE");

        markTraversal.depthFirstTraverse(g, vA);

        assertArrayEquals("Incorrect: depth-first",
                new String[]{ "bA", "aB", "aF", "bB", "aC", "bF", "aE", "cA",
                              "bC", "aD", "aE", "cB", "bE", "cF", "bD", "cC", 
                              "cE", "cD" },
                visitList.toArray());
    }

    @Test
    public void depthFirstDirected2() {
        DirectedGraph<Character, String> g =
            new DirectedGraph<Character, String>();

        LinkedList<String> visitList = new LinkedList<String>();
        Traversal<Character, String> markTraversal =
            new MarkTraversal(visitList);

        Graph<Character, String>.Vertex vA = g.add('A');
        Graph<Character, String>.Vertex vB = g.add('B');
        Graph<Character, String>.Vertex vC = g.add('C');
        Graph<Character, String>.Vertex vD = g.add('D');
        Graph<Character, String>.Vertex vE = g.add('E');
        Graph<Character, String>.Vertex vF = g.add('F');

        addEdge(g, vA, vB, "AB");
        addEdge(g, vA, vC, "AC");
        addEdge(g, vA, vD, "AD");
        addEdge(g, vD, vE, "DE");
        addEdge(g, vD, vF, "DF");

        markTraversal.depthFirstTraverse(g, vA);

        assertArrayEquals("Incorrect: depth-first",
                new String[] { "bA", "aB", "aC", "aD", "bB", "bC", "bD", "aE",
                               "aF", "cA", "cB", "cC", "bE", "bF", "cD", "cE",
                               "cF" },
                visitList.toArray());
    }

    @Test
    public void depthFirstUndirected1() {
        UndirectedGraph<Character, String> g =
            new UndirectedGraph<Character, String>();

        LinkedList<String> visitList = new LinkedList<String>();
        Traversal<Character, String> markTraversal =
            new MarkTraversal(visitList);

        Graph<Character, String>.Vertex vA = g.add('A');
        Graph<Character, String>.Vertex vB = g.add('B');
        Graph<Character, String>.Vertex vC = g.add('C');
        Graph<Character, String>.Vertex vD = g.add('D');
        Graph<Character, String>.Vertex vE = g.add('E');
        Graph<Character, String>.Vertex vF = g.add('F');

        addEdge(g, vA, vB, "AB");
        addEdge(g, vA, vF, "AF");
        addEdge(g, vB, vC, "BC");
        addEdge(g, vC, vD, "CD");
        addEdge(g, vC, vE, "CE");
        addEdge(g, vD, vB, "DB");
        addEdge(g, vD, vE, "DE");
        addEdge(g, vE, vA, "EA");
        addEdge(g, vF, vE, "FE");

        markTraversal.depthFirstTraverse(g, vA);

        assertArrayEquals("Incorrect: depth-first",
                new String[]{ "bA", "aB", "aF", "aE", "bB", "aC", "aD", "bF",
                              "aE", "bE", "aC", "aD", "cA", "bC", "aD", "bD",
                              "cB", "cF", "cE", "cC", "cD" },
                visitList.toArray());
    }

    @Test
    public void breadthFirstDirected() {
        DirectedGraph<Character, String> g =
            new DirectedGraph<Character, String>();

        LinkedList<String> visitList = new LinkedList<String>();
        Traversal<Character, String> markTraversal =
            new MarkTraversal(visitList);

        Graph<Character, String>.Vertex vA = g.add('A');
        Graph<Character, String>.Vertex vB = g.add('B');
        Graph<Character, String>.Vertex vC = g.add('C');
        Graph<Character, String>.Vertex vD = g.add('D');
        Graph<Character, String>.Vertex vE = g.add('E');
        Graph<Character, String>.Vertex vF = g.add('F');

        addEdge(g, vA, vF, "AF");
        addEdge(g, vA, vB, "AB");
        addEdge(g, vB, vC, "BC");
        addEdge(g, vC, vE, "CE");
        addEdge(g, vC, vD, "CD");
        addEdge(g, vD, vE, "DE");
        addEdge(g, vD, vB, "DB");
        addEdge(g, vE, vA, "EA");
        addEdge(g, vF, vE, "FE");

        markTraversal.breadthFirstTraverse(g, vA);

        assertArrayEquals("Incorrect: depth-first",
                new String[]{ "bA", "aF", "aB", "bF", "aE", "bB", "aC", "cA",
                              "bE", "cF", "bC", "aD", "cB", "cE", "bD", "cC",
                              "cD" },
                visitList.toArray());
    }

    @Test
    public void breadthFirstUndirected1() {
        UndirectedGraph<Character, String> g =
            new UndirectedGraph<Character, String>();

        LinkedList<String> visitList = new LinkedList<String>();
        Traversal<Character, String> markTraversal =
            new MarkTraversal(visitList);

        Graph<Character, String>.Vertex vA = g.add('A');
        Graph<Character, String>.Vertex vB = g.add('B');
        Graph<Character, String>.Vertex vC = g.add('C');
        Graph<Character, String>.Vertex vD = g.add('D');
        Graph<Character, String>.Vertex vE = g.add('E');
        Graph<Character, String>.Vertex vF = g.add('F');

        addEdge(g, vA, vB, "AB");
        addEdge(g, vA, vF, "AF");
        addEdge(g, vB, vC, "BC");
        addEdge(g, vC, vD, "CD");
        addEdge(g, vC, vE, "CE");
        addEdge(g, vD, vB, "DB");
        addEdge(g, vD, vE, "DE");
        addEdge(g, vE, vA, "EA");
        addEdge(g, vF, vE, "FE");

        markTraversal.breadthFirstTraverse(g, vA);
 
        assertArrayEquals("Incorrect: depth-first",
                new String[]{ "bA", "aB", "aF", "aE", "bB", "aC", "aD", "bF", 
                              "aE", "bE", "aC", "aD", "cA", "bC", "aD", "bD",
                              "cB", "cF", "cE", "cC", "cD" },
                visitList.toArray());
    }

    @Test
    public void breadthFirstUndirected2() {
        UndirectedGraph<Character, String> g =
            new UndirectedGraph<Character, String>();

        LinkedList<String> visitList = new LinkedList<String>();
        Traversal<Character, String> markTraversal =
            new MarkTraversal(visitList);

        Graph<Character, String>.Vertex vA = g.add('A');
        Graph<Character, String>.Vertex vB = g.add('B');
        Graph<Character, String>.Vertex vC = g.add('C');
        Graph<Character, String>.Vertex vD = g.add('D');
        Graph<Character, String>.Vertex vE = g.add('E');
        Graph<Character, String>.Vertex vF = g.add('F');

        addEdge(g, vA, vB, "AB");
        addEdge(g, vA, vC, "AC");
        addEdge(g, vA, vD, "AD");
        addEdge(g, vD, vE, "DE");
        addEdge(g, vD, vF, "DF");

        markTraversal.breadthFirstTraverse(g, vA);

        assertArrayEquals("Incorrect: depth-first",
                new String[]{ "bA", "aB", "aC", "aD", "bB", "bC", "bD", "aE", 
                              "aF", "cA", "cB", "cC", "bE", "bF", "cD", "cE",
                              "cF" },
                visitList.toArray());
    }

    private static class MarkTraversal extends Traversal<Character, String> {
        MarkTraversal(LinkedList<String> visitList) {
            _visitList = visitList;
        }

        @Override
        protected void preVisit(Graph<Character, String>.Edge e,
                                Graph<Character, String>.Vertex v) {
            _visitList.add("a" + v.toString());
        }

        @Override
        protected void visit(Graph<Character, String>.Vertex v) {
            _visitList.add("b" + v.toString());
        }

        @Override
        protected void postVisit(Graph<Character, String>.Vertex v) {
            _visitList.add("c" + v.toString());
        }

        private final LinkedList<String> _visitList;
    }

    private static <VLabel, ELabel> Graph<VLabel, ELabel>.Edge 
        addEdge(Graph<VLabel, ELabel> g,
                Graph<VLabel, ELabel>.Vertex v0,
                Graph<VLabel, ELabel>.Vertex v1,
                ELabel el) {
        return g.add(v0, v1, el);
    }
}
