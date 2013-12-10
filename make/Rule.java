package make;

import java.util.HashSet;
import java.util.Set;

/** Represents a Rule for a target in a Makefile.
 *  @author Aleks Kamko
 */
class Rule {

    /** Constructs a rule with target TARGET. */
    Rule(String target) {
        _target = target;
        _commandSet = new HashSet<String>();
        _prereqSet = new HashSet<String>();
    }

    /** Returns the target of this rule. */
    public String target() {
        return _target;
    }

    /** Returns the prerequisite set for this rule. */
    public Set<String> prereqSet() {
        return _prereqSet;
    }

    /** Returns the target set for this rule. */
    public Set<String> commandSet() {
        return _commandSet;
    }

    /** Adds PREREQ to this Rule's prerequisite set. */
    public void addPrereq(String prereq) {
        _prereqSet.add(prereq);
    }

    /** Adds COMMAND to this Rule's command set. */
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
