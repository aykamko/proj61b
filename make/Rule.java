package make;

import java.util.TreeSet;
import java.util.Set;

class Rule {

    Rule(String target) {
        _target = target;
        _commandSet = new TreeSet<String>();
    }

    public String target() {
        return _target;
    }

    public Set<String> commandSet() {
        return _commandSet;
    }

    public void addCommand(String command) {
        _commandSet.add(command);
    }

    @Override
    public String toString() {
        return String.format("%s: %s", _target, _commandSet);
    }

    /** Target for this rule. */
    private final String _target;
    /** Command set for this rule. */
    private Set<String> _commandSet;

}
