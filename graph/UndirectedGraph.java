package graph;

/* Do not add or remove public or protected members, or modify the signatures of
 * any public methods.  You may add bodies to abstract methods, modify
 * existing bodies, or override inherited methods.  */

/** An undirected graph with vertices labeled with VLABEL and edges
 *  labeled with ELABEL.
 *  @author Aleks Kamko
 */
public class UndirectedGraph<VLabel, ELabel> extends Graph<VLabel, ELabel> {

    /** An empty graph. */
    public UndirectedGraph() {
        super();
    }

    @Override
    public boolean isDirected() {
        return false;
    }

    @Override
    public int outDegree(Vertex v) {
        throw new
            UnsupportedOperationException("not supported on undirected graph");
    }

    @Override
    public int inDegree(Vertex v) {
        throw new
            UnsupportedOperationException("not supported on undirected graph");
    }

    @Override
    public Iteration<Vertex> successors(Vertex v) {
        throw new
            UnsupportedOperationException("not supported on undirected graph");
    }

    @Override
    public Iteration<Vertex> predecessors(Vertex v) {
        throw new
            UnsupportedOperationException("not supported on undirected graph");
    }

    @Override
    public Iteration<Edge> outEdges(Vertex v) {
        throw new
            UnsupportedOperationException("not supported on undirected graph");
    }

    @Override
    public Iteration<Edge> inEdges(Vertex v) {
        throw new
            UnsupportedOperationException("not supported on undirected graph");
    }

}
