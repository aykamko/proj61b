package make;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import java.util.HashSet;
import java.util.Set;

import java.util.HashMap;
import java.util.Map;

import graph.Graph;
import graph.DirectedGraph;
import graph.NoLabel;
import graph.Traversal;

/** Class that builds targets for the 'make' program and updates targets'
 *  changedate.
 *  @author Aleks Kamko
 */
public class TargetBuilder {

    /** Constructor for TargetBuilder. RULELIST is a list of rules to build
     *  TARGETLIST with. CHANGEMAP maps objects to their last build times. */
    TargetBuilder(List<Rule> ruleList, Map<String, Long> changeMap,
            List<String> targetList) {
        _ruleList = ruleList;
        _changeMap = changeMap;
        _targetList = targetList;
        _builtSet = new HashSet<String>();
    }

    /** Builds this TargetBuilder's targets. */
    public void buildTargets() {
        constructGraph();
        _sortedTargets = topologicalSort(_depGraph);

        for (String target : _targetList) {
            buildTarget(target, true);
        }
        
    }

    private void buildTarget(String target, boolean forceBuild) {
        if (_builtSet.contains(target)) {
            return;
        }

        Rule rule = ruleForTarget(target);
        Long age = _changeMap.get(target);
        if (rule == null) {
            if (age == null) {
                String error = String.format(
                        "target '%s' does not exist", target);
                throw new MakeException(error);
            } else {
                return;
            }
        }

        boolean build = forceBuild;
        if (age == null) {
            build = true;
        }

        Long prereqAge;
        for (String prereq : rule.prereqSet()) {
            buildTarget(prereq, false);
            prereqAge = _changeMap.get(prereq);
            if ((age != null && prereqAge != null) && prereqAge > age) {
                build = true;
            }
        }

        if (build) {
            for (String cmnd : rule.commandSet()) {
                //FIXME?
                System.out.println(cmnd);
            }
            _builtSet.add(target);
        }
    }

    /** Returns the Rule associated with the target, or null if no rule
     *  exists. */
    private Rule ruleForTarget(String target) {
        Rule result = null;
        for (Rule r: _ruleList) {
            if (r.target().equals(target)) {
                result = r;
                break;
            }
        }
        return result;
    }

    /** Checks that all targets exist. Throws a MakeException otherwise. */
    private void checkTargets() throws MakeException {
        boolean hasTarget = false;
        for (String target : _targetList) {
            if (_changeMap.get(target) == null) {
                for (Rule rule : _ruleList) {
                    if (rule.target().equals(target)) {
                        hasTarget = true;
                        break;
                    }
                }
                if (!hasTarget) {
                    String err = String.format(
                            "target '%s' does not exist", target);
                    throw new MakeException(err);
                }
            }
            hasTarget = false;
        }
    }

    /** Constructs a dependency graph from _ruleList. */
    private void constructGraph() {
        _depGraph = new DirectedGraph<InCountLabel, NoLabel>();

        Map<String, Graph<InCountLabel, NoLabel>.Vertex> addedMap =
            new HashMap<String, Graph<InCountLabel, NoLabel>.Vertex>();

        String target = null;
        Set<String> commandSet;
        List<Graph<InCountLabel, NoLabel>.Vertex> vlist;
        for (Rule rule : _ruleList) {
            vlist = new LinkedList<Graph<InCountLabel, NoLabel>.Vertex>();
            target = rule.target();
            commandSet = rule.commandSet();

            for (String cmnd : commandSet) {
                Graph<InCountLabel, NoLabel>.Vertex v = 
                    addedMap.get(cmnd);
                if (v == null) {
                    v = _depGraph.add(new InCountLabel(cmnd));
                    addedMap.put(cmnd, v);
                }
                vlist.add(v);
            }

            Graph<InCountLabel, NoLabel>.Vertex t = addedMap.get(target);
            if (t == null) {
                t = _depGraph.add(new InCountLabel(target));
                addedMap.put(target, t);
            }

            for (Graph<InCountLabel, NoLabel>.Vertex u : vlist) {
                _depGraph.add(u, t);
            }

        }

        for (Graph<InCountLabel, NoLabel>.Vertex v : _depGraph.vertices()) {
            InCountLabel vlabel = v.getLabel();
            vlabel.setInCount(_depGraph.inDegree(v));
        }
    }

    /** Returns a list of the vertices in graph G in topologically sorted
     *  order. */
    private static List<String> 
        topologicalSort(Graph<InCountLabel, NoLabel> g) {

        List<String> sorted = new ArrayList<String>();

        LinkedList<Graph<InCountLabel, NoLabel>.Vertex> fringe =
            new LinkedList<Graph<InCountLabel, NoLabel>.Vertex>();
        for (Graph<InCountLabel, NoLabel>.Vertex v : g.vertices()) {
            if (v.getLabel().inCount() == 0) {
                fringe.add(v);
            }
        }
        if (fringe.isEmpty()) {
            throw new MakeException("circular dependency in makefile");
        }

        Graph<InCountLabel, NoLabel>.Vertex v;
        while (!fringe.isEmpty()) {
            v = fringe.pollFirst();
            sorted.add(v.toString());
            for (Graph<InCountLabel, NoLabel>.Vertex u : g.successors(v)) {
                InCountLabel uLabel = u.getLabel();
                uLabel.decrementInCount();
                if (uLabel.inCount() == 0) {
                    fringe.add(u);
                }
            }
        }

        return sorted;
    }

    /** Class for labeling vertices. InCountLabel has a String label to
     *  designate a vertex, and an inCount() representing the number of
     *  incoming edges to the vertex. */
    private static class InCountLabel {

        /** Constructs an InCountLabel with LABEL and an inCount() of -1. */
        InCountLabel(String label) {
            _label = label;
            _inCount = -1;
        }

        @Override
        public String toString() {
            return _label;
        }

        /** Sets _inCount to INCOUNT. */
        public void setInCount(int inCount) {
            _inCount = inCount;
        }

        /** Decrements _inCount by 1. */
        public void decrementInCount() {
            _inCount -= 1;
        }

        /** Returns an int representing the number of incoming edges to the
         *  labeled vertex. */
        public int inCount() {
            return _inCount;
        }

        @Override
        public int hashCode() {
            return _label.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof InCountLabel) {
                InCountLabel compareLabel = (InCountLabel) obj;
                return _label.equals(obj.toString());
            }
            return false;
        }

        /** Label for the vertex. */
        private String _label;
        /** Number of incoming edges to the vertex. */
        private int _inCount;
    }

    /** Dependency graph with vertices representing targets and directed edges
     *  representing dependencies. */
    private DirectedGraph<InCountLabel, NoLabel> _depGraph;
    /** List of targets in topologically sorted dependency order. */
    private List<String> _sortedTargets;
    /** List of targets to build. */
    private final List<String> _targetList;
    /** List of rules to build targets with. */
    private final List<Rule> _ruleList;
    /** Maps objects (targets) to last build time. */
    private final Map<String, Long> _changeMap;
    /** Stores already built objects. */
    private final Set<String> _builtSet;

}
