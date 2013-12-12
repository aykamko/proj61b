package make;

import java.util.LinkedList;
import java.util.List;

import java.util.HashSet;
import java.util.Set;

import java.util.HashMap;
import java.util.Map;

import graph.Graph;
import graph.DirectedGraph;
import graph.NoLabel;

import static make.MakeException.error;

/** Class that builds targets for the 'make' program and updates targets'
 *  changedate.
 *  @author Aleks Kamko
 */
public class TargetBuilder {

    /** Constructor for TargetBuilder. RULEMAP is a map from targets to Rules
     *  to build TARGETLIST with. CHANGEMAP maps objects to their last
     *  build times. */
    TargetBuilder(Map<String, Rule> ruleMap, Map<String, Long> changeMap,
            List<String> targetList) {
        _ruleMap = ruleMap;
        _changeMap = changeMap;
        _targetList = targetList;
        _builtSet = new HashSet<String>();
    }

    /** Builds this TargetBuilder's targets. */
    public void buildTargets() {
        checkMakefileUsingGraph();
        for (String target : _targetList) {
            buildTarget(target, true);
        }
    }

    /** Builds TARGET if FORCEBUILD is true, if it has at least one younger
     *  prerequisite, or if it has no previously recorded changedate. Throws
     *  a MakeException if an error is encountered with a target. */
    private void buildTarget(String target, boolean forceBuild) {
        if (_builtSet.contains(target)) {
            return;
        }

        Rule rule = _ruleMap.get(target);
        Long age = _changeMap.get(target);
        if (rule == null) {
            if (age == null) {
                throw error("target '%s' does not exist", target);
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
            if (age != null && prereqAge != null && prereqAge > age) {
                build = true;
            }
        }

        if (build) {
            for (String cmnd : rule.commandList()) {
                System.out.println(cmnd);
            }
            _builtSet.add(target);
        }
    }

    /** Constructs a dependency graph from _ruleList to check for circular
     *  dependencies. Throws a MakeException if a circular dependency is
     *  detected. */
    private void checkMakefileUsingGraph() {
        DirectedGraph<String, NoLabel> depGraph =
            new DirectedGraph<String, NoLabel>();
        Map<String, Graph<String, NoLabel>.Vertex> addedMap =
            new HashMap<String, Graph<String, NoLabel>.Vertex>();

        String target = null;
        Set<String> prereqSet;
        List<Graph<String, NoLabel>.Vertex> vlist;
        for (Rule rule : _ruleMap.values()) {
            vlist = new LinkedList<Graph<String, NoLabel>.Vertex>();
            target = rule.target();
            prereqSet = rule.prereqSet();

            for (String prereq : prereqSet) {
                Graph<String, NoLabel>.Vertex v = addedMap.get(prereq);
                if (v == null) {
                    v = depGraph.add(prereq);
                    addedMap.put(prereq, v);
                }
                vlist.add(v);
            }

            Graph<String, NoLabel>.Vertex t = addedMap.get(target);
            if (t == null) {
                t = depGraph.add(target);
                addedMap.put(target, t);
            }

            for (Graph<String, NoLabel>.Vertex u : vlist) {
                if (depGraph.contains(t, u)) {
                    throw new MakeException("circular dependency detected");
                }
                depGraph.add(u, t);
            }
        }
    }

    /** List of targets to build. */
    private final List<String> _targetList;
    /** Maps target names to their respective rules. */
    private final Map<String, Rule> _ruleMap;
    /** Maps objects (targets) to last build time. */
    private final Map<String, Long> _changeMap;
    /** Stores already built objects. */
    private final Set<String> _builtSet;

}
