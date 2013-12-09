package make;

import java.util.HashSet;
import java.util.Set;

class Rule {

    Rule(String target) {
        _target = target;
        _commandSet = new HashSet<String>();
        _prereqSet = new HashSet<String>();
    }

    public String target() {
        return _target;
    }

    public Set<String> prereqSet() {
        return _prereqSet;
    }

    public Set<String> commandSet() {
        return _commandSet;
    }

    public void addPrereq(String prereq) {
        _prereqSet.add(prereq);
    }

    public void addCommand(String command) {
        _commandSet.add(command);
    }

    @Override
    public String toString() {
        return String.format("%s: %s", _target, _prereqSet);
    }

    /** Target for this rule. */
    private final String _target;
    /** Prerequisite set for this rule. */
    private Set<String> _prereqSet;
    /** Command set for this rule. */
    private Set<String> _commandSet;

}
