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

        addEdge(g, vA, vF, "AF");
        addEdge(g, vA, vB, "AB");
        addEdge(g, vB, vC, "BC");
        addEdge(g, vC, vE, "CE");
        addEdge(g, vC, vD, "CD");
        addEdge(g, vD, vE, "DE");
        addEdge(g, vD, vB, "DB");
        addEdge(g, vE, vA, "EA");
        addEdge(g, vF, vE, "FE");

        markTraversal.depthFirstTraverse(g, vA);

        assertArrayEquals("Incorrect: depth-first",
                new String[]{ "bA", "aF", "aB", "bB", "aC", "bC", "aE", "aD",
                              "bD", "aE", "aB", "bE", "aA", "cE", "cD", "cC",
                              "cB", "bF", "aE", "cF", "cA" },
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
                new String[] { "bA", "aB", "aC", "aD", "bD", "aE", "aF", "bF",
                               "cF", "bE", "cE", "cD", "bC", "cC", "bB", "cB",
                               "cA" },
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

        addEdge(g, vA, vF, "AF");
        addEdge(g, vA, vB, "AB");
        addEdge(g, vB, vC, "BC");
        addEdge(g, vC, vE, "CE");
        addEdge(g, vC, vD, "CD");
        addEdge(g, vD, vE, "DE");
        addEdge(g, vD, vB, "DB");
        addEdge(g, vE, vA, "EA");
        addEdge(g, vF, vE, "FE");

        markTraversal.depthFirstTraverse(g, vA);

        assertArrayEquals("Incorrect: depth-first",
                new String[]{ "bA", "aF", "aB", "aE", "bE", "aC", "aD", "aA", 
                              "aF", "bF", "aA", "aE", "cF", "bD", "aC", "aE",
                              "aB", "bB", "aA", "aC", "aD", "bC", "aB", "aE",
                              "aD", "cC", "cB", "cD", "cE", "cA" },
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
                              "bE", "aA", "cF", "bC", "aE", "aD", "cB", "cE",
                              "bD", "aE", "aB", "cC", "cD" },
                visitList.toArray());
    }

    @Test
    public void breadthFirstUndirected() {
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
                new String[]{ "bA", "aF", "aB", "aE", "bF", "aA", "aE", "bB", 
                              "aA", "aC", "aD", "bE", "aC", "aD", "aA", "aF",
                              "cA", "cE", "cF", "bC", "aB", "aE", "aD", "bD",
                              "aC", "aE", "aB", "cB", "cC", "cD" },
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
