package graph;

/* Do not add or remove public or protected members, or modify the signatures of
 * any public methods.  You may add bodies to abstract methods, modify
 * existing bodies, or override inherited methods.  */

import java.util.Set;
import java.util.HashSet;

/** A directed graph with vertices labeled with VLABEL and edges
 *  labeled with ELABEL.
 *  @author Aleks Kamko
 */
public class DirectedGraph<VLabel, ELabel> extends Graph<VLabel, ELabel> {

    /** An empty graph. */
    public DirectedGraph() {
        super();
    }

    @Override
    public boolean isDirected() {
        return true;
    }

    @Override
    public Edge add(Vertex from,
                    Vertex to,
                    ELabel label) {
        Edge e = new Edge(from, to, label);
        from.addOutgoingEdge(e);
        to.addIncomingEdge(e);
        edgeSet().add(e);
        return e;
    }

    @Override
    public void remove(Vertex v) {
        Vertex other;
        Set<Edge> elist = new HashSet<Edge>();
        for (Edge e : outEdges(v)) {
            elist.add(e);
        }
        for (Edge e : inEdges(v)) {
            elist.add(e);
        }
        for (Edge e : elist) {
            remove(e);
        }
        vertexSet().remove(v);
    }

    @Override
    public void remove(Edge e) {
        e.getV0().removeOutgoing(e);
        e.getV1().removeIncoming(e);
        edgeSet().remove(e);
    }

}
