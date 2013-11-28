package make;

import java.util.ArrayList;
import java.util.List;

class Rule {

    Rule(String target) {
        _target = target;
        _commandSet = new ArrayList<String>();
    }

    public void addCommand(String command) {
        _commandSet.add(command);
    }

    /** Target for this rule. */
    private final String _target;
    /** Command set for this rule. */
    private List<String> _commandSet;

}
