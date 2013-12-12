package make;

import java.util.HashSet;
import java.util.Set;

import java.util.LinkedList;
import java.util.List;

/** Represents a Rule for a target in a Makefile.
 *  @author Aleks Kamko
 */
class Rule {

    /** Constructs a rule with target TARGET. */
    Rule(String target) {
        _target = target;
        _commandList = new LinkedList<String>();
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

    /** Returns the command list for this rule. */
    public List<String> commandList() {
        return _commandList;
    }

    /** Adds PREREQ to this Rule's prerequisite set. */
    public void addPrereq(String prereq) {
        _prereqSet.add(prereq);
    }

    /** Adds COMMAND to this Rule's command list. */
    public void addCommand(String command) {
        _commandList.add(command);
    }

    @Override
    public String toString() {
        return String.format("%s: %s", _target, _prereqSet);
    }

    /** Target for this rule. */
    private final String _target;
    /** Prerequisite set for this rule. */
    private Set<String> _prereqSet;
    /** Command list for this rule. */
    private List<String> _commandList;

}
